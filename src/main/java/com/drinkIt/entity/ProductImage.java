package com.drinkIt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Original drink/product name.
     *
     * Example:
     * Kingfisher Beer
     */
    @Column(nullable = false, unique = true, length = 255)
    private String productName;

    /*
     * Normalized name used for searching.
     *
     * Example:
     * kingfisher beer
     */
    @Column(nullable = false, unique = true, length = 255)
    private String normalizedName;

    /*
     * Actual file name stored on server.
     */
    @Column(nullable = false, length = 255)
    private String fileName;

    /*
     * Public URL returned to frontend.
     *
     * Example:
     * http://localhost:8080/uploads/products/kingfisher-beer.jpg
     */
    @Column(nullable = false, length = 1000)
    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}