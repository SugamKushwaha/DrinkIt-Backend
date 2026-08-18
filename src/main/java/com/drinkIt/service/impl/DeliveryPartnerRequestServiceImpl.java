package com.drinkIt.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drinkIt.dto.delivery.DeliveryPartnerRequestResponse;
import com.drinkIt.entity.DeliveryPartnerRequest;
import com.drinkIt.entity.User;
import com.drinkIt.enums.RequestStatus;
import com.drinkIt.enums.Role;
import com.drinkIt.repository.DeliveryPartnerRequestRepository;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.service.DeliveryPartnerRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryPartnerRequestServiceImpl
        implements DeliveryPartnerRequestService {

    private final UserRepository userRepository;

    private final DeliveryPartnerRequestRepository
            deliveryPartnerRequestRepository;

    @Override
    public DeliveryPartnerRequestResponse apply(
            Long userId,
            DeliveryPartnerRequestResponse request
    ) {

        User user = getUser(userId);

        if (user.getRole() != Role.CUSTOMER) {

            throw new RuntimeException(
                    "Only customers can apply for delivery partner"
            );
        }

        if (deliveryPartnerRequestRepository
                .existsByUserIdAndStatus(
                        userId,
                        RequestStatus.PENDING
                )) {

            throw new RuntimeException(
                    "Delivery partner request already pending"
            );
        }

        DeliveryPartnerRequest deliveryRequest =
                DeliveryPartnerRequest.builder()

                        .user(user)

                        .address(request.getAddress())

                        .city(request.getCity())

                        .state(request.getState())

                        .pincode(request.getPincode())

                        .vehicleType(
                                request.getVehicleType()
                        )

                        .vehicleNumber(
                                request.getVehicleNumber()
                        )

                        .drivingLicenseNumber(
                                request.getDrivingLicenseNumber()
                        )

                        .aadhaarNumber(
                                request.getAadhaarNumber()
                        )

                        .status(RequestStatus.PENDING)

                        .requestedAt(LocalDateTime.now())

                        .build();

        return convert(
                deliveryPartnerRequestRepository
                        .save(deliveryRequest)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryPartnerRequestResponse>
    getPendingRequests() {

        return deliveryPartnerRequestRepository
                .findByStatus(RequestStatus.PENDING)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryPartnerRequestResponse
    getRequest(Long requestId) {

        return convert(getRequestEntity(requestId));
    }

    @Override
    public DeliveryPartnerRequestResponse
    approve(Long requestId) {

        DeliveryPartnerRequest request =
                getRequestEntity(requestId);

        if (request.getStatus() != RequestStatus.PENDING) {

            throw new RuntimeException(
                    "Request already processed"
            );
        }

        User user = request.getUser();

        /*
         * Customer becomes Delivery Partner.
         */

        user.setRole(Role.DELIVERY_PARTNER);

        userRepository.save(user);

        request.setStatus(RequestStatus.APPROVED);

        request.setProcessedAt(
                LocalDateTime.now()
        );

        return convert(
                deliveryPartnerRequestRepository
                        .save(request)
        );
    }

    @Override
    public DeliveryPartnerRequestResponse
    reject(
            Long requestId,
            String reason
    ) {

        DeliveryPartnerRequest request =
                getRequestEntity(requestId);

        if (request.getStatus() != RequestStatus.PENDING) {

            throw new RuntimeException(
                    "Request already processed"
            );
        }

        request.setStatus(RequestStatus.REJECTED);

        request.setRejectionReason(reason);

        request.setProcessedAt(
                LocalDateTime.now()
        );

        return convert(
                deliveryPartnerRequestRepository
                        .save(request)
        );
    }

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    private DeliveryPartnerRequest
    getRequestEntity(Long requestId) {

        return deliveryPartnerRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Delivery partner request not found"
                        )
                );
    }

    private DeliveryPartnerRequestResponse
    convert(DeliveryPartnerRequest request) {

        User user = request.getUser();

        return DeliveryPartnerRequestResponse.builder()

                .requestId(request.getId())

                .userId(user.getId())

                .name(user.getName())

                .email(user.getEmail())

                .phone(user.getPhone())

                .address(request.getAddress())

                .city(request.getCity())

                .state(request.getState())

                .pincode(request.getPincode())

                .vehicleType(
                        request.getVehicleType()
                )

                .vehicleNumber(
                        request.getVehicleNumber()
                )

                .drivingLicenseNumber(
                        request.getDrivingLicenseNumber()
                )

                .aadhaarNumber(
                        request.getAadhaarNumber()
                )

                .status(request.getStatus())

                .build();
    }

    @Override
@Transactional(readOnly = true)
public DeliveryPartnerRequestResponse
getRequestByUser(Long userId) {

    DeliveryPartnerRequest request =
            deliveryPartnerRequestRepository
                    .findByUserId(userId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Delivery partner request not found"
                            )
                    );

    return convert(request);
}
}