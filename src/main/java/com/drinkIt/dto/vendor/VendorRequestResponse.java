package com.drinkIt.dto.vendor;

import com.drinkIt.enums.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorRequestResponse {

    private Long requestId;

    private Long userId;

    private String name;

    private String email;

    private String phone;

    private String businessName;

    private String businessType;

    private String businessAddress;

    private String city;

    private String state;

    private String pincode;

    private String gstNumber;

    private String licenseNumber;

    private RequestStatus status;

    private String rejectionReason;
}