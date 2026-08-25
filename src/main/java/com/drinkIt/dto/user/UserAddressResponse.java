package com.drinkIt.dto.user;

import com.drinkIt.enums.AddressType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponse {

    private Long id;
    
    private AddressType addressType;

    private String fullName;

    private String phone;

    private String addressLine;

    private String city;

    private String state;

    private String pincode;
    
}
