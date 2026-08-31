package com.drinkIt.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.drinkIt.dto.admin.DeliveryPartnerResponse;
import com.drinkIt.dto.admin.VendorResponse;
import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.entity.DeliveryPartner;
import com.drinkIt.entity.User;
import com.drinkIt.entity.Vendor;
import com.drinkIt.enums.Role;
import com.drinkIt.repository.DeliveryPartnerRepository;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.repository.VendorRepository;
import com.drinkIt.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

     private final VendorRepository vendorRepository;

     private final DeliveryPartnerRepository deliveryPartnerRepository;


     @Override
    public List<UserResponse> allCustomers() {

        return userRepository.findByRole(Role.CUSTOMER)
                .stream()
                .map(user -> {

                    UserResponse response = new UserResponse();

                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setPhone(user.getPhone());
                    response.setCreatedAt(user.getCreatedAt());

                    return response;
                })
                .toList();
            }


      @Override
    public List<VendorResponse> allVendors() {

        return vendorRepository
                .findAll()
                .stream()
                .map(this::mapVendor)
                .toList();
    }

    @Override
    public VendorResponse getVendor(Long id) {

        Vendor vendor = vendorRepository.findById(id).orElseThrow(() ->new RuntimeException("Vendor not found" ));

        return mapVendor(vendor);
    }

    @Override
    public List<DeliveryPartnerResponse>
    allDeliveryPartners() {

        return deliveryPartnerRepository
                .findAll()
                .stream()
                .map(this::mapDeliveryPartner)
                .toList();
    }
  
      @Override
    public DeliveryPartnerResponse
    getDeliveryPartner(Long id) {

        DeliveryPartner partner= deliveryPartnerRepository.findById(id).orElseThrow(() ->new RuntimeException("Delivery partner not found"  ));

        return mapDeliveryPartner(partner);
    }
    // Map Vendor

     private VendorResponse mapVendor(Vendor vendor) {

        User user = vendor.getUser();

        return VendorResponse.builder()

                .id(vendor.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .businessName(vendor.getBusinessName())
                .businessAddress(vendor.getBusinessAddress())
                .city(vendor.getCity())
                .state(vendor.getState())
                .pincode(vendor.getPincode() )
                .gstNumber(vendor.getGstNumber())
                .licenseNumber(vendor.getLicenseNumber())
                .status(vendor.getStatus())
                .createdAt(vendor.getCreatedAt())
                .build();
    }


     private DeliveryPartnerResponse mapDeliveryPartner(DeliveryPartner partner) {

        User user = partner.getUser();

        return DeliveryPartnerResponse
                .builder()

                .id(partner.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(partner.getAddress() )
                .city(partner.getCity())
                .state(partner.getState())
                .pincode( partner.getPincode())
                .vehicleType(partner.getVehicleType())
                .vehicleNumber(partner.getVehicleNumber())
                .drivingLicenseNumber(partner.getDrivingLicenseNumber())
                .aadhaarNumber(partner.getAadhaarNumber())
                .status(partner.getStatus() )
                .build();
    }
}