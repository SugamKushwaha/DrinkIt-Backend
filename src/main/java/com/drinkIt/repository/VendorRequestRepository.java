package com.drinkIt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.User;
import com.drinkIt.entity.VendorRequest;
import com.drinkIt.enums.RequestStatus;

public interface VendorRequestRepository
        extends JpaRepository<VendorRequest, Long> {

    Optional<VendorRequest> findByUser(User user);

    Optional<VendorRequest> findByUserId(Long userId);

    List<VendorRequest> findByStatus(RequestStatus status);

    boolean existsByUserIdAndStatus(
            Long userId,
            RequestStatus status
    );
}