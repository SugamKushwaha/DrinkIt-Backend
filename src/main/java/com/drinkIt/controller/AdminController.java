package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.delivery.DeliveryPartnerRequestResponse;
import com.drinkIt.dto.vendor.VendorRequestResponse;
import com.drinkIt.service.DeliveryPartnerRequestService;
import com.drinkIt.service.VendorRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VendorRequestService vendorRequestService;

    private final DeliveryPartnerRequestService
            deliveryPartnerRequestService;

    // ==========================
    // VENDOR REQUESTS
    // ==========================

    @GetMapping("/vendor-requests")
    public ResponseEntity<
            List<VendorRequestResponse>
            > getVendorRequests() {

        return ResponseEntity.ok(
                vendorRequestService
                        .getPendingRequests()
        );
    }

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

    // ==========================
    // DELIVERY REQUESTS
    // ==========================

    @GetMapping("/delivery-partner-requests")
    public ResponseEntity<
            List<DeliveryPartnerRequestResponse>
            > getDeliveryRequests() {

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
}