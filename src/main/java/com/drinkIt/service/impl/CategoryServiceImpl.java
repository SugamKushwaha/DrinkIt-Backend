package com.drinkIt.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drinkIt.dto.category.CategoryRequest;
import com.drinkIt.dto.category.CategoryResponse;
import com.drinkIt.entity.Category;
import com.drinkIt.repository.CategoryRepository;
import com.drinkIt.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository categoryRepository;

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public CategoryResponse create(
            CategoryRequest request
    ) {

        validate(request);

        if (categoryRepository
                .existsByNameIgnoreCase(request.getName())) {

            throw new RuntimeException(
                    "Category already exists"
            );
        }

        Category category =
                Category.builder()
                        .name(request.getName().trim())
                        .image(request.getImage())
                        .active(
                                request.getActive() != null
                                        ? request.getActive()
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
    public CategoryResponse getById(Long id) {

        return map(findCategory(id));
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public CategoryResponse update(
            Long id,
            CategoryRequest request
    ) {

        validate(request);

        Category category =
                findCategory(id);

        if (!category.getName()
                .equalsIgnoreCase(request.getName())
                && categoryRepository
                        .existsByNameIgnoreCase(
                                request.getName()
                        )) {

            throw new RuntimeException(
                    "Category already exists"
            );
        }

        category.setName(
                request.getName().trim()
        );

        category.setImage(
                request.getImage()
        );

        category.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        return map(
                categoryRepository.save(category)
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public void delete(Long id) {

        Category category =
                findCategory(id);

        categoryRepository.delete(category);
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private void validate(
            CategoryRequest request
    ) {

        if (request.getName() == null
                || request.getName().isBlank()) {

            throw new RuntimeException(
                    "Category name is required"
            );
        }

        if (request.getImage() == null
                || request.getImage().isBlank()) {

            throw new RuntimeException(
                    "Category image is required"
            );
        }
    }

    // =====================================================
    // FIND
    // =====================================================

    private Category findCategory(Long id) {

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