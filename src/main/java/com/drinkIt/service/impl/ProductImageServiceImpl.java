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

import com.drinkIt.dto.product.ProductImageResponse;
import com.drinkIt.entity.ProductImage;
import com.drinkIt.repository.ProductImageRepository;
import com.drinkIt.service.ProductImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl
        implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    @Value("${app.upload.directory:uploads/products}")
    private String uploadDirectory;

    @Value("${app.server.base-url:http://localhost:8080}")
    private String serverBaseUrl;


    // =====================================================
    // UPLOAD IMAGE
    // =====================================================

    @Override
    public ProductImageResponse uploadImage(
            String productName,
            MultipartFile image
    ) {

        if (productName == null
                || productName.isBlank()) {

            throw new RuntimeException(
                    "Product name is required"
            );
        }

        if (image == null
                || image.isEmpty()) {

            throw new RuntimeException(
                    "Product image is required"
            );
        }


        String cleanProductName =
                productName.trim();

        String normalizedName =
                normalizeName(
                        cleanProductName
                );


        // -------------------------------------------------
        // VALIDATE IMAGE
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
        // FIND EXISTING IMAGE
        // -------------------------------------------------

        ProductImage productImage =
                productImageRepository
                        .findByNormalizedName(
                                normalizedName
                        )
                        .orElse(null);


        String fileName;

        if (productImage != null) {

            /*
             * Existing product image.
             *
             * Replace the old file.
             */
            fileName =
                    productImage.getFileName();

        } else {

            /*
             * New product image.
             */
            fileName =
                    normalizedName
                            .replaceAll(
                                    "[^a-z0-9]+",
                                    "-"
                            )
                            .replaceAll(
                                    "^-|-$",
                                    ""
                            )
                            + "-"
                            + UUID.randomUUID()
                                    .toString()
                                    .substring(0, 8)
                            + extension;
        }


        // -------------------------------------------------
        // CREATE DIRECTORY
        // -------------------------------------------------

        try {

            Path uploadPath =
                    Paths.get(
                            uploadDirectory
                    ).toAbsolutePath()
                            .normalize();

            Files.createDirectories(
                    uploadPath
            );


            // -------------------------------------------------
            // SAVE FILE
            // -------------------------------------------------

            Path target =
                    uploadPath.resolve(
                            fileName
                    ).normalize();


            /*
             * Prevent path traversal.
             */
            if (!target.getParent()
                    .equals(uploadPath)) {

                throw new RuntimeException(
                        "Invalid image file"
                );
            }


            try (InputStream inputStream =
                         image.getInputStream()) {

                Files.copy(
                        inputStream,
                        target,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }


            String imageUrl =
                    serverBaseUrl
                            .replaceAll(
                                    "/$",
                                    ""
                            )
                            + "/uploads/products/"
                            + fileName;


            // -------------------------------------------------
            // SAVE DATABASE
            // -------------------------------------------------

            if (productImage == null) {

                productImage =
                        ProductImage.builder()
                                .productName(
                                        cleanProductName
                                )
                                .normalizedName(
                                        normalizedName
                                )
                                .fileName(
                                        fileName
                                )
                                .imageUrl(
                                        imageUrl
                                )
                                .build();

            } else {

                productImage.setProductName(
                        cleanProductName
                );

                productImage.setFileName(
                        fileName
                );

                productImage.setImageUrl(
                        imageUrl
                );
            }


            productImage =
                    productImageRepository.save(
                            productImage
                    );


            return map(productImage);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to save product image",
                    e
            );
        }
    }


    // =====================================================
    // GET IMAGE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProductImageResponse getImageByProductName(
            String productName
    ) {

        if (productName == null
                || productName.isBlank()) {

            throw new RuntimeException(
                    "Product name is required"
            );
        }


        String normalizedName =
                normalizeName(productName);


        ProductImage image =
                productImageRepository
                        .findByNormalizedName(
                                normalizedName
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "No image found for product: "
                                                + productName
                                )
                        );


        return map(image);
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getAllImages() {

        return productImageRepository
                .findAll()
                .stream()
                .map(this::map)
                .toList();
    }


    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public void deleteImage(Long id) {

        ProductImage productImage =
                productImageRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product image not found"
                                )
                        );


        try {

            Path path =
                    Paths.get(
                            uploadDirectory,
                            productImage.getFileName()
                    )
                    .toAbsolutePath()
                    .normalize();


            Files.deleteIfExists(path);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to delete image file",
                    e
            );
        }


        productImageRepository.delete(
                productImage
        );
    }


    // =====================================================
    // NORMALIZE NAME
    // =====================================================

    private String normalizeName(
            String name
    ) {

        return name
                .trim()
                .toLowerCase()
                .replaceAll(
                        "\\s+",
                        " "
                );
    }


    // =====================================================
    // EXTENSION
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

    private ProductImageResponse map(
            ProductImage image
    ) {

        return ProductImageResponse.builder()

                .id(image.getId())

                .productName(
                        image.getProductName()
                )

                .imageUrl(
                        image.getImageUrl()
                )

                .fileName(
                        image.getFileName()
                )

                .createdAt(
                        image.getCreatedAt()
                )

                .updatedAt(
                        image.getUpdatedAt()
                )

                .build();
    }
}