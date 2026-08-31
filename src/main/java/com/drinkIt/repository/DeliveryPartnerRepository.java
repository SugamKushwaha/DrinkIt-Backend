package com.drinkIt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.DeliveryPartner;

public interface DeliveryPartnerRepository
        extends JpaRepository<DeliveryPartner, Long> {

    Optional<DeliveryPartner> findByUserId(
            Long userId
    );

    boolean existsByUserId(
            Long userId
    );
}