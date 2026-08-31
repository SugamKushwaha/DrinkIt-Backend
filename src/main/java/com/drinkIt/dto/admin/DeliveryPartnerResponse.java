package com.drinkIt.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerResponse {

    private Long id;

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

    private String status;
}