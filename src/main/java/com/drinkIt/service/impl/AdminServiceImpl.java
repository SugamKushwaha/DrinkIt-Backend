package com.drinkIt.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drinkIt.dto.admin.AdminRequest;
import com.drinkIt.dto.admin.AdminResponse;
import com.drinkIt.dto.admin.DeliveryPartnerResponse;
import com.drinkIt.dto.admin.VendorResponse;
import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.entity.Admin;
import com.drinkIt.entity.DeliveryPartner;
import com.drinkIt.entity.User;
import com.drinkIt.entity.Vendor;
import com.drinkIt.enums.Role;
import com.drinkIt.repository.AdminRepository;
import com.drinkIt.repository.DeliveryPartnerRepository;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.repository.VendorRepository;
import com.drinkIt.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

     private final AdminRepository adminRepository;

     private final VendorRepository vendorRepository;

     private final DeliveryPartnerRepository deliveryPartnerRepository;

     private final PasswordEncoder passwordEncoder;


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
    public List<DeliveryPartnerResponse> allDeliveryPartners() {

        return deliveryPartnerRepository
                .findAll()
                .stream()
                .map(this::mapDeliveryPartner)
                .toList();
    }
  
    @Override
    public DeliveryPartnerResponse getDeliveryPartner(Long id) {

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


    // admin section


     @Override
    public AdminResponse createAdmin(
            AdminRequest request
    ) {

        // ------------------------------
        // VALIDATE
        // ------------------------------

        if (
                request.getName() == null
                ||
                request.getName().isBlank()
        ) {

            throw new RuntimeException(
                    "Admin name is required"
            );
        } if (
                request.getEmail() == null
                ||
                request.getEmail().isBlank()
        ) {

            throw new RuntimeException(
                    "Admin email is required"
            );
        }

          String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

          if (
                userRepository
                        .existsByEmail(email)
        ) {

            throw new RuntimeException(
                    "User already exists with this email"
            );
        }
          String adminRole =
                request.getRole();


        if (
                adminRole == null
                ||
                adminRole.isBlank()
        ) {

            adminRole = "ADMIN";
        }
          if (
                !adminRole.equals("ADMIN")
                &&
                !adminRole.equals("PRODUCT_ADMIN")
                &&
                !adminRole.equals("PARTNER_ADMIN")
        ) {

            throw new RuntimeException(
                    "Invalid admin role"
            );
        }

         User user =
        User.builder()

                .name(
                        request.getName().trim()
                )

                .email(email)

                .phone(
                        request.getPhone()
                )

                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )

                .role(
                        Role.ADMIN
                )

                .createdAt(
                        LocalDateTime.now()
                )

                .build();


        user =
                userRepository.save(user);


                 Admin admin =
                Admin.builder()

                        .user(user)

                        .adminRole(
                                adminRole
                        )

                        .status(
                                "ACTIVE"
                        )

                        .createdAt(
                                LocalDateTime.now()
                        )

                        .build();


        admin =
                adminRepository.save(admin);


        return mapAdmin(admin);
    }

   
    // get all admins

      @Override
    @Transactional(readOnly = true)
    public List<AdminResponse>
    getAllAdmins() {

        return adminRepository
                .findAll()

                .stream()

                .map(this::mapAdmin)

                .toList();
    }


    // get admin by ud 

      @Override
    @Transactional(readOnly = true)
    public AdminResponse
    getAdmin(Long id) {

        Admin admin =
                adminRepository
                        .findById(id)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Admin not found"
                                )
                        );


        return mapAdmin(admin);
    }

    // Delete Admin

   @Override
public void deleteAdmin(Long id) {

    Admin admin =
            adminRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Admin not found"
                            )
                    );


    if ("SUPER_ADMIN".equals(
            admin.getAdminRole()
    )) {

        throw new RuntimeException(
                "Super Admin cannot be deleted"
        );
    }


    User user =
            admin.getUser();


    adminRepository.delete(admin);


    userRepository.delete(user);
}


      private AdminResponse
    mapAdmin(
            Admin admin
    ) {

        User user =
                admin.getUser();


        return AdminResponse
                .builder()

                .id(
                        admin.getId()
                )

                .userId(
                        user.getId()
                )

                .name(
                        user.getName()
                )
                 .email(
                        user.getEmail()
                )

                .phone(
                        user.getPhone()
                )

                .role(
                        admin.getAdminRole()
                )

                .status(
                        admin.getStatus()
                )

                .createdAt(
                        admin.getCreatedAt()
                )

                .build();
    }


}