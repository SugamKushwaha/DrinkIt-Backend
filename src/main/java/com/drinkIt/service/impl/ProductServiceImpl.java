package com.drinkIt.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drinkIt.dto.product.ProductRequest;
import com.drinkIt.dto.product.ProductResponse;
import com.drinkIt.entity.Product;
import com.drinkIt.entity.ProductImage;
import com.drinkIt.entity.User;
import com.drinkIt.entity.Vendor;
import com.drinkIt.enums.ProductStatus;
import com.drinkIt.repository.ProductImageRepository;
import com.drinkIt.repository.ProductRepository;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.repository.VendorRepository;
import com.drinkIt.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final VendorRepository vendorRepository;

    private final ProductImageRepository productImageRepository;

    private String normalizeProductName( String name) {

    return name
            .trim()
            .toLowerCase()
            .replaceAll(
                    "\\s+",
                    " "
            );
}


    // =====================================================
    // ADMIN CREATE
    // =====================================================

    @Override
    public ProductResponse createByAdmin(ProductRequest request) {

        validateProduct(request);

      ProductImage productImage =
        productImageRepository
                .findByNormalizedName(
                        normalizeProductName(
                                request.getName()
                        )
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Please upload an image for this product first"
                        )
                );


Product product = Product.builder()

        .name(request.getName())

        .brand(request.getBrand())

        .category(request.getCategory())

        .volume(request.getVolume())

        .price(request.getPrice())

        .oldPrice(request.getOldPrice())

        .description(request.getDescription())

        .image(productImage.getImageUrl())

        .stock(request.getStock())

        .popular(
                request.getPopular() != null
                        ? request.getPopular()
                        : false
        )

        .status(resolveStatus(request))

        .vendor(null)

        .build();

        product = productRepository.save(product);

        return map(product);
    }


    // =====================================================
    // ADMIN GET ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllForAdmin() {

        return productRepository
                .findAll()
                .stream()
                .map(this::map)
                .toList();
    }


    // =====================================================
    // ADMIN GET ONE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getAdminProduct(
            Long id
    ) {

        Product product =
                findProduct(id);

        return map(product);
    }


    // =====================================================
    // ADMIN UPDATE
    // =====================================================

    @Override
    public ProductResponse updateByAdmin(
            Long id,
            ProductRequest request
    ) {

        validateProduct(request);

        Product product =
                findProduct(id);

        updateProduct(
                product,
                request,
                true
        );

        return map(
                productRepository.save(product)
        );
    }


    // =====================================================
    // ADMIN DELETE
    // =====================================================

    @Override
    public void deleteByAdmin(
            Long id
    ) {

        Product product =
                findProduct(id);

        productRepository.delete(product);
    }


    // =====================================================
    // ADMIN TOGGLE STATUS
    // =====================================================

    @Override
    public ProductResponse toggleStatusByAdmin(
            Long id
    ) {

        Product product =
                findProduct(id);

        if (product.getStock() != null
                && product.getStock() == 0) {

            product.setStatus(
                    ProductStatus.OUT_OF_STOCK
            );

        } else if (
                product.getStatus()
                        == ProductStatus.ACTIVE
        ) {

            product.setStatus(
                    ProductStatus.HIDDEN
            );

        } else {

            product.setStatus(
                    ProductStatus.ACTIVE
            );
        }

        return map(
                productRepository.save(product)
        );
    }


    // =====================================================
    // VENDOR CREATE
    // =====================================================

    @Override
    public ProductResponse createByVendor(
            ProductRequest request,
            Long userId
    ) {

        validateProduct(request);

        User user =
                userRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        Vendor vendor =
                vendorRepository
                        .findByUserId(user.getId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Vendor profile not found"
                                )
                        );
 
                        ProductImage productImage =
        productImageRepository
                .findByNormalizedName(
                        normalizeProductName(
                                request.getName()
                        )
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "No image has been uploaded by admin for product: "
                                        + request.getName()
                        )
                );
Product product = Product.builder()

        .name(request.getName())

        .brand(request.getBrand())

        .category(request.getCategory())

        .volume(request.getVolume())

        .price(request.getPrice())

        .oldPrice(request.getOldPrice())

        .description(request.getDescription())

        .image(productImage.getImageUrl())

        .stock(request.getStock())

        .popular(
                request.getPopular() != null
                        ? request.getPopular()
                        : false
        )

        .status(resolveStatus(request))

        .vendor(vendor)

        .build();

        product = productRepository.save(product);

        return map(product);
    }


    // =====================================================
    // VENDOR GET OWN PRODUCTS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getVendorProducts(
            Long userId
    ) {

        Vendor vendor =
                getVendorByUserId(userId);

        return productRepository
                .findByVendor(vendor)
                .stream()
                .map(this::map)
                .toList();
    }


    // =====================================================
    // VENDOR GET ONE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getVendorProduct(
            Long id,
            Long userId
    ) {

        Vendor vendor =
                getVendorByUserId(userId);

        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product not found"
                                )
                        );

        checkOwnership(
                product,
                vendor
        );

        return map(product);
    }


    // =====================================================
    // VENDOR UPDATE
    // =====================================================

    @Override
    public ProductResponse updateByVendor(
            Long id,
            ProductRequest request,
            Long userId
    ) {

        validateProduct(request);

        Vendor vendor =
                getVendorByUserId(userId);

        Product product =
                findProduct(id);

        checkOwnership(
                product,
                vendor
        );

        updateProduct(
                product,
                request,
                false
        );

        return map(
                productRepository.save(product)
        );
    }


    // =====================================================
    // VENDOR DELETE
    // =====================================================

    @Override
    public void deleteByVendor(
            Long id,
            Long userId
    ) {

        Vendor vendor =
                getVendorByUserId(userId);

        Product product =
                findProduct(id);

        checkOwnership(
                product,
                vendor
        );

        productRepository.delete(product);
    }


    // =====================================================
    // VENDOR TOGGLE
    // =====================================================

    @Override
    public ProductResponse toggleStatusByVendor(
            Long id,
            Long userId
    ) {

        Vendor vendor =
                getVendorByUserId(userId);

        Product product =
                findProduct(id);

        checkOwnership(
                product,
                vendor
        );

        if (product.getStock() != null
                && product.getStock() == 0) {

            product.setStatus(
                    ProductStatus.OUT_OF_STOCK
            );

        } else if (
                product.getStatus()
                        == ProductStatus.ACTIVE
        ) {

            product.setStatus(
                    ProductStatus.HIDDEN
            );

        } else {

            product.setStatus(
                    ProductStatus.ACTIVE
            );
        }

        return map(
                productRepository.save(product)
        );
    }


    // =====================================================
    // CUSTOMER - ACTIVE PRODUCTS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {

        return productRepository
                .findByStatus(
                        ProductStatus.ACTIVE
                )
                .stream()
                .map(this::map)
                .toList();
    }


    // =====================================================
    // CUSTOMER - ONE PRODUCT
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getActiveProduct(
            Long id
    ) {

        Product product =
                findProduct(id);

        if (product.getStatus()
                != ProductStatus.ACTIVE) {

            throw new RuntimeException(
                    "Product is not available"
            );
        }

        return map(product);
    }


    // =====================================================
    // CUSTOMER - CATEGORY
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse>
    getActiveProductsByCategory(
            String category
    ) {

        return productRepository
                .findByCategoryIgnoreCaseAndStatus(
                        category,
                        ProductStatus.ACTIVE
                )
                .stream()
                .map(this::map)
                .toList();
    }


    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

    private void updateProduct(
        Product product,
        ProductRequest request,
        boolean allowImageChange
) {

    product.setName(
            request.getName()
    );

    product.setBrand(
            request.getBrand()
    );

    product.setCategory(
            request.getCategory()
    );

    product.setVolume(
            request.getVolume()
    );

    product.setPrice(
            request.getPrice()
    );

    product.setOldPrice(
            request.getOldPrice()
    );

    product.setDescription(
            request.getDescription()
    );

    if (allowImageChange) {

        ProductImage productImage =
                productImageRepository
                        .findByNormalizedName(
                                normalizeProductName(
                                        request.getName()
                                )
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Please upload an image for this product first"
                                )
                        );

        product.setImage(
                productImage.getImageUrl()
        );
    }

    product.setStock(
            request.getStock()
    );

    product.setPopular(
            request.getPopular() != null
                    ? request.getPopular()
                    : false
    );

    product.setStatus(
            resolveStatus(request)
    );
}


    // =====================================================
    // STATUS RESOLUTION
    // =====================================================

    private ProductStatus resolveStatus(
            ProductRequest request
    ) {

        if (request.getStock() != null
                && request.getStock() == 0) {

            return ProductStatus.OUT_OF_STOCK;
        }

        if (request.getStatus()
                == ProductStatus.HIDDEN) {

            return ProductStatus.HIDDEN;
        }

        return ProductStatus.ACTIVE;
    }


    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateProduct(
            ProductRequest request
    ) {

        if (request.getName() == null
                || request.getName().isBlank()) {

            throw new RuntimeException(
                    "Product name is required"
            );
        }

        if (request.getCategory() == null
                || request.getCategory().isBlank()) {

            throw new RuntimeException(
                    "Product category is required"
            );
        }

        if (request.getPrice() == null
                || request.getPrice().signum() < 0) {

            throw new RuntimeException(
                    "Valid product price is required"
            );
        }

        if (request.getStock() == null
                || request.getStock() < 0) {

            throw new RuntimeException(
                    "Valid product stock is required"
            );
        }
    }


    // =====================================================
    // FIND PRODUCT
    // =====================================================

    private Product findProduct(
            Long id
    ) {

        return productRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found with id: "
                                        + id
                        )
                );
    }


    // =====================================================
    // FIND VENDOR
    // =====================================================

    private Vendor getVendorByUserId(
            Long userId
    ) {

        return vendorRepository
                .findByUserId(userId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Vendor profile not found"
                        )
                );
    }


    // =====================================================
    // OWNERSHIP CHECK
    // =====================================================

    private void checkOwnership(
            Product product,
            Vendor vendor
    ) {

        if (product.getVendor() == null
                || !product.getVendor()
                        .getId()
                        .equals(vendor.getId())) {

            throw new RuntimeException(
                    "You are not allowed to manage this product"
            );
        }
    }


    // =====================================================
    // MAP RESPONSE
    // =====================================================

    private ProductResponse map(
            Product product
    ) {

        Long vendorId = null;

        String vendorName = null;

        if (product.getVendor() != null) {

            vendorId =
                    product.getVendor().getId();

            vendorName =
                    product.getVendor()
                            .getBusinessName();
        }

        return ProductResponse.builder()

                .id(product.getId())

                .name(product.getName())

                .brand(product.getBrand())

                .category(product.getCategory())

                .volume(product.getVolume())

                .price(product.getPrice())

                .oldPrice(product.getOldPrice())

                .description(product.getDescription())

                .image(product.getImage())

                .stock(product.getStock())

                .popular(product.getPopular())

                .status(product.getStatus())

                .vendorId(vendorId)

                .vendorName(vendorName)

                .createdAt(product.getCreatedAt())

                .updatedAt(product.getUpdatedAt())

                .build();
    }
}