package com.drinkIt.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.drinkIt.dto.category.CategoryResponse;
import com.drinkIt.entity.Category;
import com.drinkIt.repository.CategoryRepository;
import com.drinkIt.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Value("${app.server.base-url:http://localhost:8080}")
    private String serverBaseUrl;


    // =====================================================
    // UPLOAD / REPLACE CATEGORY
    // =====================================================

    @Override
    public CategoryResponse uploadImage(
            String categoryName,
            MultipartFile image
    ) {

        // -------------------------------------------------
        // CATEGORY NAME
        // -------------------------------------------------

        if (categoryName == null
                || categoryName.isBlank()) {

            throw new RuntimeException(
                    "Category name is required"
            );
        }


        // -------------------------------------------------
        // IMAGE
        // -------------------------------------------------

        if (image == null
                || image.isEmpty()) {

            throw new RuntimeException(
                    "Category image is required"
            );
        }


        String cleanName =
                categoryName.trim();


        // -------------------------------------------------
        // IMAGE TYPE
        // -------------------------------------------------

        String contentType =
                image.getContentType();

        if (contentType == null
                || !contentType.startsWith("image/")) {

            throw new RuntimeException(
                    "Only image files are allowed"
            );
        }


        // -------------------------------------------------
        // FILE EXTENSION
        // -------------------------------------------------

        String originalFileName =
                StringUtils.cleanPath(
                        image.getOriginalFilename()
                );


        String extension =
                getExtension(
                        originalFileName
                );


        if (extension.isBlank()) {

            throw new RuntimeException(
                    "Image file extension is required"
            );
        }


        // -------------------------------------------------
        // FIND EXISTING CATEGORY
        // -------------------------------------------------

        Category category =
                categoryRepository
                        .findByNameIgnoreCase(
                                cleanName
                        )
                        .orElse(null);


        String fileName;


        // -------------------------------------------------
        // EXISTING CATEGORY
        // -------------------------------------------------

        if (category != null) {

            String oldFileName =
                    getFileName(
                            category.getImage()
                    );


            if (oldFileName != null
                    && !oldFileName.isBlank()) {

                fileName = oldFileName;

            } else {

                fileName =
                        generateFileName(
                                cleanName,
                                extension
                        );
            }

        }

        // -------------------------------------------------
        // NEW CATEGORY
        // -------------------------------------------------

        else {

            fileName =
                    generateFileName(
                            cleanName,
                            extension
                    );
        }


        // -------------------------------------------------
        // CATEGORY DIRECTORY
        // -------------------------------------------------

        Path uploadPath =
                Paths.get(
                        "uploads/categories"
                )
                .toAbsolutePath()
                .normalize();


        try {

            Files.createDirectories(
                    uploadPath
            );


            // -------------------------------------------------
            // TARGET FILE
            // -------------------------------------------------

            Path target =
                    uploadPath
                            .resolve(fileName)
                            .normalize();


            // Security check
            if (!target.getParent()
                    .equals(uploadPath)) {

                throw new RuntimeException(
                        "Invalid image file"
                );
            }


            // -------------------------------------------------
            // SAVE FILE
            // -------------------------------------------------

            try (
                    InputStream inputStream =
                            image.getInputStream()
            ) {

                Files.copy(
                        inputStream,
                        target,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }


            // -------------------------------------------------
            // IMAGE URL
            // -------------------------------------------------

            String imageUrl =
                    serverBaseUrl
                            .replaceAll(
                                    "/$",
                                    ""
                            )
                            + "/uploads/categories/"
                            + fileName;


            // -------------------------------------------------
            // CREATE CATEGORY
            // -------------------------------------------------

            if (category == null) {

                category =
                        Category.builder()
                                .name(cleanName)
                                .image(imageUrl)
                                .active(true)
                                .build();

            }

            // -------------------------------------------------
            // UPDATE CATEGORY
            // -------------------------------------------------

            else {

                category.setName(
                        cleanName
                );

                category.setImage(
                        imageUrl
                );
            }


            // -------------------------------------------------
            // DATABASE
            // -------------------------------------------------

            category =
                    categoryRepository.save(
                            category
                    );


            return map(category);


        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to save category image",
                    e
            );
        }
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllImages() {

        return categoryRepository
                .findAll()
                .stream()
                .map(this::map)
                .toList();
    }


    // =====================================================
    // GET BY NAME
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getImageByCategoryName(
            String categoryName
    ) {

        if (categoryName == null
                || categoryName.isBlank()) {

            throw new RuntimeException(
                    "Category name is required"
            );
        }


        Category category =
                categoryRepository
                        .findByNameIgnoreCase(
                                categoryName.trim()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Category not found: "
                                                + categoryName
                                )
                        );


        return map(category);
    }


    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public void deleteImage(Long id) {

        Category category =
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Category not found"
                                )
                        );


        String fileName =
                getFileName(
                        category.getImage()
                );


        if (fileName != null
                && !fileName.isBlank()) {

            try {

                Path path =
                        Paths.get(
                                "uploads/categories",
                                fileName
                        )
                        .toAbsolutePath()
                        .normalize();


                Files.deleteIfExists(path);

            } catch (IOException e) {

                throw new RuntimeException(
                        "Unable to delete category image",
                        e
                );
            }
        }


        categoryRepository.delete(
                category
        );
    }


    // =====================================================
    // GENERATE FILE NAME
    // =====================================================

    private String generateFileName(
            String categoryName,
            String extension
    ) {

        String normalized =
                categoryName
                        .toLowerCase()
                        .replaceAll(
                                "[^a-z0-9]+",
                                "-"
                        )
                        .replaceAll(
                                "^-|-$",
                                ""
                        );


        return normalized
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .substring(
                                0,
                                8
                        )
                + extension;
    }


    // =====================================================
    // GET FILE NAME FROM URL
    // =====================================================

    private String getFileName(
            String imageUrl
    ) {

        if (imageUrl == null
                || imageUrl.isBlank()) {

            return null;
        }


        int index =
                imageUrl.lastIndexOf("/");


        if (index == -1) {

            return imageUrl;
        }


        return imageUrl.substring(
                index + 1
        );
    }


    // =====================================================
    // GET EXTENSION
    // =====================================================

    private String getExtension(
            String fileName
    ) {

        if (fileName == null
                || fileName.isBlank()) {

            return "";
        }


        int index =
                fileName.lastIndexOf(".");


        if (index == -1) {

            return "";
        }


        return fileName
                .substring(index)
                .toLowerCase();
    }


    // =====================================================
    // MAP
    // =====================================================

    private CategoryResponse map(
            Category category
    ) {

        return CategoryResponse.builder()

                .id(category.getId())

                .categoryName(
                        category.getName()
                )

                .imageUrl(
                        category.getImage()
                )

                .fileName(
                        getFileName(
                                category.getImage()
                        )
                )

                .createdAt(
                        category.getCreatedAt()
                )

                .updatedAt(
                        category.getUpdatedAt()
                )

                .build();
    }
}