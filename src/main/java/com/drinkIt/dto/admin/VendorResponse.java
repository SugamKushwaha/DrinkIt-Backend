package com.drinkIt.dto.admin;

import java.time.LocalDateTime;

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
public class VendorResponse {

    private Long id;

    private Long userId;

    private String name;

    private String email;

    private String phone;

    private String businessName;

    private String businessAddress;

    private String city;

    private String state;

    private String pincode;

    private String gstNumber;

    private String licenseNumber;

    private String status;

    private LocalDateTime createdAt;
}