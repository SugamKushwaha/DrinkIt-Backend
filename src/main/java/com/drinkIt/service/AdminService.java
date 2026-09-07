package com.drinkIt.service;

import java.util.List;

import com.drinkIt.dto.admin.AdminRequest;
import com.drinkIt.dto.admin.AdminResponse;
import com.drinkIt.dto.admin.DeliveryPartnerResponse;
import com.drinkIt.dto.admin.VendorResponse;
import com.drinkIt.dto.user.UserResponse;

public interface AdminService {
    
    List<UserResponse> allCustomers();

    List<VendorResponse> allVendors();

    VendorResponse getVendor(Long id);

    List<DeliveryPartnerResponse> allDeliveryPartners();


    DeliveryPartnerResponse getDeliveryPartner(Long id);

     AdminResponse createAdmin(AdminRequest request);


    List<AdminResponse>
    getAllAdmins();


    AdminResponse getAdmin(Long id);


    void deleteAdmin(Long id);
}
