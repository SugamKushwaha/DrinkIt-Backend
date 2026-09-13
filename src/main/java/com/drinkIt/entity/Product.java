package com.drinkIt.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.drinkIt.enums.ProductStatus;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // BASIC INFORMATION
    // =====================================================

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column(nullable = false)
    private String category;

    private String volume;


    // =====================================================
    // PRICE
    // =====================================================

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal oldPrice;


    // =====================================================
    // PRODUCT INFORMATION
    // =====================================================

    @Column(length = 3000)
    private String description;

    @Column(length = 1000)
    private String image;


    // =====================================================
    // STOCK
    // =====================================================

    @Column(nullable = false)
    private Integer stock;


    // =====================================================
    // POPULAR
    // =====================================================

    @Column(nullable = false)
    private Boolean popular = false;


    // =====================================================
    // STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;


    // =====================================================
    // VENDOR
    // =====================================================

    /*
     * ADMIN product:
     *
     * vendor = null
     *
     * VENDOR product:
     *
     * vendor = logged-in vendor
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;


    // =====================================================
    // CREATED / UPDATED
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        updatedAt = LocalDateTime.now();

        if (popular == null) {
            popular = false;
        }

        if (status == null) {

            if (stock != null && stock == 0) {
                status = ProductStatus.OUT_OF_STOCK;
            } else {
                status = ProductStatus.ACTIVE;
            }
        }
    }


    // =====================================================
    // PRE UPDATE
    // =====================================================

    @PreUpdate
    public void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}