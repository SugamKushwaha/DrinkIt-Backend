package com.drinkIt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    List<Category> findByActiveTrue();

    Optional<Category> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}