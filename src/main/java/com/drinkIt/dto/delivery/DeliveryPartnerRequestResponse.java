package com.drinkIt.dto.delivery;

import com.drinkIt.enums.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerRequestResponse {

    private Long requestId;

    private Long userId;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String vehicleType;

    private String vehicleNumber;

    private String drivingLicenseNumber;

    private String aadhaarNumber;

    private RequestStatus status;
}