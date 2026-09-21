package com.drinkIt.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.drinkIt.dto.category.CategoryResponse;
import com.drinkIt.entity.Category;
import com.drinkIt.repository.CategoryRepository;
import com.drinkIt.service.CategoryService;
import com.drinkIt.service.ImageStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ImageStorageService imageStorageService;

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public CategoryResponse create(
            String name,
            MultipartFile image,
            Boolean active
    ) {

        validateName(name);
        validateImage(image);

        if (categoryRepository
                .existsByNameIgnoreCase(name.trim())) {

            throw new RuntimeException(
                    "Category already exists"
            );
        }

        String imagePath =
                imageStorageService
                        .saveCategoryImage(image);

        Category category =
                Category.builder()
                        .name(name.trim())
                        .image(imagePath)
                        .active(
                                active != null
                                        ? active
                                        : true
                        )
                        .build();

        return map(
                categoryRepository.save(category)
        );
    }

    // =====================================================
    // GET ALL - ADMIN
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {

        return categoryRepository
                .findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // =====================================================
    // GET ACTIVE - CUSTOMER
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActive() {

        return categoryRepository
                .findByActiveTrue()
                .stream()
                .map(this::map)
                .toList();
    }

    // =====================================================
    // GET ONE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(
            Long id
    ) {

        return map(findCategory(id));
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public CategoryResponse update(
            Long id,
            String name,
            MultipartFile image,
            Boolean active
    ) {

        validateName(name);

        Category category =
                findCategory(id);

        // Check duplicate name
        if (!category.getName()
                .equalsIgnoreCase(name.trim())
                && categoryRepository
                        .existsByNameIgnoreCase(
                                name.trim()
                        )) {

            throw new RuntimeException(
                    "Category already exists"
            );
        }

        category.setName(
                name.trim()
        );

        // Only replace image when
        // admin selected a new image
        if (image != null
                && !image.isEmpty()) {

            String oldImage =
                    category.getImage();

            String newImage =
                    imageStorageService
                            .saveCategoryImage(image);

            category.setImage(
                    newImage
            );

            // Delete old image
            imageStorageService
                    .deleteImage(oldImage);
        }

        category.setActive(
                active != null
                        ? active
                        : category.getActive()
        );

        return map(
                categoryRepository.save(category)
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public void delete(
            Long id
    ) {

        Category category =
                findCategory(id);

        String image =
                category.getImage();

        categoryRepository.delete(category);

        // Delete image from server
        imageStorageService
                .deleteImage(image);
    }

    // =====================================================
    // VALIDATE NAME
    // =====================================================

    private void validateName(
            String name
    ) {

        if (name == null
                || name.isBlank()) {

            throw new RuntimeException(
                    "Category name is required"
            );
        }
    }

    // =====================================================
    // VALIDATE IMAGE
    // =====================================================

    private void validateImage(
            MultipartFile image
    ) {

        if (image == null
                || image.isEmpty()) {

            throw new RuntimeException(
                    "Category image is required"
            );
        }

        String contentType =
                image.getContentType();

        if (contentType == null
                || !contentType.startsWith("image/")) {

            throw new RuntimeException(
                    "Only image files are allowed"
            );
        }
    }

    // =====================================================
    // FIND
    // =====================================================

    private Category findCategory(
            Long id
    ) {

        return categoryRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Category not found with id: "
                                        + id
                        )
                );
    }

    // =====================================================
    // MAP
    // =====================================================

    private CategoryResponse map(
            Category category
    ) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .image(category.getImage())
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}