package com.drinkIt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.DeliveryPartnerRequest;
import com.drinkIt.entity.User;
import com.drinkIt.enums.RequestStatus;

public interface DeliveryPartnerRequestRepository
        extends JpaRepository<DeliveryPartnerRequest, Long> {

    Optional<DeliveryPartnerRequest> findByUser(User user);

    Optional<DeliveryPartnerRequest> findByUserId(Long userId);

    List<DeliveryPartnerRequest> findByStatus(RequestStatus status);

    boolean existsByUserIdAndStatus(
            Long userId,
            RequestStatus status
    );
}