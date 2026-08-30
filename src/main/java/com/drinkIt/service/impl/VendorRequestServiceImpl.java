package com.drinkIt.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drinkIt.dto.vendor.VendorRequestResponse;
import com.drinkIt.entity.User;
import com.drinkIt.entity.Vendor;
import com.drinkIt.entity.VendorRequest;
import com.drinkIt.enums.RequestStatus;
import com.drinkIt.enums.Role;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.repository.VendorRepository;
import com.drinkIt.repository.VendorRequestRepository;
import com.drinkIt.service.VendorRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorRequestServiceImpl implements VendorRequestService {

    private final UserRepository userRepository;

    private final VendorRequestRepository vendorRequestRepository;

    private final VendorRepository vendorRepository;

    @Override
    public VendorRequestResponse apply(
            Long userId,
            VendorRequestResponse request
    ) {

        User user = getUser(userId);

        if (user.getRole() == Role.VENDOR) {

        throw new RuntimeException(
                "You are already a vendor."
        );
    }

        if (user.getRole() != Role.CUSTOMER) {

            throw new RuntimeException(
                    "Only customers can apply for vendor"
            );
        }


   String gstNumber = request.getGstNumber();

    if (gstNumber != null) {
        gstNumber = gstNumber.trim().toUpperCase();
    }

    String licenseNumber = request.getLicenseNumber();

    if (licenseNumber != null) {
        licenseNumber = licenseNumber.trim().toUpperCase();
    }

        if (vendorRequestRepository
            .existsByUserIdAndStatus(
                    userId,
                    RequestStatus.PENDING
            )) {

        throw new RuntimeException(
                "Application already exists. Your vendor application is already pending."
        );
    }

      if (gstNumber != null
            && !gstNumber.isBlank()
            && vendorRequestRepository
                    .existsByGstNumberAndStatusNot(
                            gstNumber,
                            RequestStatus.REJECTED
                    )) {

        throw new RuntimeException(
                "Application already exists with this GST number."
        );
    }

      if (gstNumber != null
            && !gstNumber.isBlank()
            && vendorRequestRepository
                    .existsByGstNumberAndStatusNot(
                            gstNumber,
                            RequestStatus.REJECTED
                    )) {

        throw new RuntimeException(
                "Application already exists with this GST number."
        );
    }

        VendorRequest vendorRequest =
                VendorRequest.builder()

                        .user(user)

                        .businessName(request.getBusinessName() )

                        .businessAddress(request.getBusinessAddress() )

                        .city(request.getCity())

                        .state(request.getState())

                        .pincode(request.getPincode())

                        .gstNumber(gstNumber)

                        .licenseNumber(licenseNumber)

                        .status(RequestStatus.PENDING)

                        .requestedAt(LocalDateTime.now())

                        .build();

        return convert(
                vendorRequestRepository.save(vendorRequest)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorRequestResponse>
    getPendingRequests() {

        return vendorRequestRepository
                .findByStatus(RequestStatus.PENDING)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VendorRequestResponse getRequest(
            Long requestId
    ) {

        return convert(getVendorRequest(requestId));
    }

    @Override
    public VendorRequestResponse approve(
            Long requestId
    ) {

        VendorRequest request =
                getVendorRequest(requestId);

        if (request.getStatus() != RequestStatus.PENDING) {

            throw new RuntimeException(
                    "Request already processed"
            );
        }

        User user = request.getUser();

        /*
         * THIS IS THE IMPORTANT PART
         *
         * Customer becomes Vendor automatically.
         */

         // Customer → Vendor
    user.setRole(Role.VENDOR);

    userRepository.save(user);

    // Create Vendor
    Vendor vendor = Vendor.builder()

            .user(user)

            .businessName(
                    request.getBusinessName()
            )

            .businessAddress(
                    request.getBusinessAddress()
            )

            .city(
                    request.getCity()
            )

            .state(
                    request.getState()
            )
             .pincode(
                    request.getPincode()
            )

            .gstNumber(
                    request.getGstNumber()
            )

            .licenseNumber(
                    request.getLicenseNumber()
            )

            .status("ACTIVE")

            .createdAt(LocalDateTime.now())

            .build();
              vendorRepository.save(vendor);

    // Update request
    request.setStatus(
            RequestStatus.APPROVED
    );

    request.setProcessedAt(
            LocalDateTime.now()
    );

        return convert(
                vendorRequestRepository.save(request)
        );
    }

    @Override
    public VendorRequestResponse reject(
            Long requestId,
            String reason
    ) {

        VendorRequest request =
                getVendorRequest(requestId);

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
                vendorRequestRepository.save(request)
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

    private VendorRequest
    getVendorRequest(Long requestId) {

        return vendorRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vendor request not found"
                        )
                );
    }

    private VendorRequestResponse
    convert(VendorRequest request) {

        User user = request.getUser();

        return VendorRequestResponse.builder()

                .requestId(request.getId())

                .userId(user.getId())

                .name(user.getName())

                .email(user.getEmail())

                .phone(user.getPhone())

                .businessName(
                        request.getBusinessName()
                )

                .businessAddress(
                        request.getBusinessAddress()
                )

                .city(request.getCity())

                .state(request.getState())

                .pincode(request.getPincode())

                .gstNumber(request.getGstNumber())

                .licenseNumber(
                        request.getLicenseNumber()
                )

                .status(request.getStatus())

                .build();
    }

    @Override
@Transactional(readOnly = true)
public VendorRequestResponse getRequestByUser(
        Long userId
) {

    VendorRequest request =
            vendorRequestRepository
                    .findByUserId(userId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Vendor request not found"
                            )
                    );

    return convert(request);
}
}
