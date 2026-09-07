package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.admin.AdminRequest;
import com.drinkIt.dto.admin.AdminResponse;
import com.drinkIt.dto.admin.DeliveryPartnerResponse;
import com.drinkIt.dto.admin.VendorResponse;
import com.drinkIt.dto.delivery.DeliveryPartnerRequestResponse;
import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.dto.vendor.VendorRequestResponse;
import com.drinkIt.service.AdminService;
import com.drinkIt.service.DeliveryPartnerRequestService;
import com.drinkIt.service.VendorRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VendorRequestService vendorRequestService;

    private final DeliveryPartnerRequestService deliveryPartnerRequestService;

    private final AdminService adminService;

// Get All Users

@GetMapping("/users")
public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(
        adminService.allCustomers() );
}


// Get All Vendors

@GetMapping("/vendors")
public ResponseEntity<List<VendorResponse>> getVendors() {

    return ResponseEntity.ok(
            adminService.allVendors()
    );
}


// Get single vendor by id

@GetMapping("/vendors/{id}")
public ResponseEntity<VendorResponse> getVendor(
        @PathVariable Long id
) {

    return ResponseEntity.ok(
            adminService.getVendor(id)
    );
}

    // Get All VENDOR REQUESTS

    @GetMapping("/vendor-requests")
    public ResponseEntity<List<VendorRequestResponse>> getVendorRequests() {

        return ResponseEntity.ok(
                vendorRequestService
                        .getPendingRequests()
        );
    }

    // Get Request By id

    @GetMapping("/vendor-requests/{id}")
    public ResponseEntity<VendorRequestResponse>
    getVendorRequest(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                vendorRequestService
                        .getRequest(id)
        );
    }

    // Approve Vendor Request
 
    @PutMapping("/vendor-requests/{id}/approve")
    public ResponseEntity<VendorRequestResponse>
    approveVendor(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                vendorRequestService
                        .approve(id)
        );
    }


    // Reject Vendor Request

    @PutMapping("/vendor-requests/{id}/reject")
    public ResponseEntity<VendorRequestResponse>
    rejectVendor(
            @PathVariable Long id,
            @RequestParam String reason
    ) {

        return ResponseEntity.ok(
                vendorRequestService
                        .reject(
                                id,
                                reason
                        )
        );
    }



    // DELIVERY REQUESTS

    @GetMapping("/delivery-partners") 
    public ResponseEntity<List<DeliveryPartnerResponse>> getDeliveryPartners() {
    
        return ResponseEntity.ok(

            adminService
                    .allDeliveryPartners()
    );
}

@GetMapping("/delivery-partners/{id}")
public ResponseEntity<DeliveryPartnerResponse> getDeliveryPartner( @PathVariable Long id ) {

    return ResponseEntity.ok(

            adminService
                    .getDeliveryPartner(id)
    );
}

    @GetMapping("/delivery-partner-requests")
    public ResponseEntity< List<DeliveryPartnerRequestResponse>> getDeliveryRequests() {

        return ResponseEntity.ok(
                deliveryPartnerRequestService
                        .getPendingRequests()
        );
    }

    @GetMapping("/delivery-partner-requests/{id}")
    public ResponseEntity<
            DeliveryPartnerRequestResponse
            > getDeliveryRequest(
                    @PathVariable Long id
            ) {

        return ResponseEntity.ok(
                deliveryPartnerRequestService
                        .getRequest(id)
        );
    }

    @PutMapping(
            "/delivery-partner-requests/{id}/approve"
    )
    public ResponseEntity<
            DeliveryPartnerRequestResponse
            > approveDeliveryPartner(
                    @PathVariable Long id
            ) {

        return ResponseEntity.ok(
                deliveryPartnerRequestService
                        .approve(id)
        );
    }

    @PutMapping(
            "/delivery-partner-requests/{id}/reject"
    )
    public ResponseEntity<
            DeliveryPartnerRequestResponse
            > rejectDeliveryPartner(
                    @PathVariable Long id,
                    @RequestParam String reason
            ) {

        return ResponseEntity.ok(
                deliveryPartnerRequestService
                        .reject(
                                id,
                                reason
                        )
        );
    }



    // admin module

    @PostMapping("/admins")
public ResponseEntity<AdminResponse>
createAdmin(

        @RequestBody
        AdminRequest request

) {

    return ResponseEntity.ok(

            adminService
                    .createAdmin(request)

    );

}

@GetMapping("/admins")
public ResponseEntity<List<AdminResponse>>
getAdmins() {

    return ResponseEntity.ok(

            adminService
                    .getAllAdmins()

    );

}

@GetMapping("/admins/{id}")
public ResponseEntity<AdminResponse>
getAdmin(

        @PathVariable Long id

) {

    return ResponseEntity.ok(

            adminService
                    .getAdmin(id)

    );

}

@DeleteMapping("/admins/{id}")
public ResponseEntity<Void>
deleteAdmin(

        @PathVariable Long id

) {

    adminService
            .deleteAdmin(id);


    return ResponseEntity
            .noContent()
            .build();

}
}