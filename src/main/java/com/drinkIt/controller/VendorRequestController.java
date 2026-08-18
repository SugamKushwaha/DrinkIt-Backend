package com.drinkIt.controller;

import com.drinkIt.dto.vendor.VendorRequestResponse;
import com.drinkIt.security.CurrentUserService;
import com.drinkIt.service.VendorRequestService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor-requests")
@RequiredArgsConstructor
public class VendorRequestController {

    private final VendorRequestService vendorRequestService;

    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<VendorRequestResponse> apply(
            Authentication authentication,
            @RequestBody VendorRequestResponse request
    ) {

        Long userId =
                currentUserService
                        .getUser(authentication)
                        .getId();

        return ResponseEntity.ok(
                vendorRequestService.apply(
                        userId,
                        request
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<VendorRequestResponse>
    myRequest(
            Authentication authentication
    ) {

        Long userId =
                currentUserService
                        .getUser(authentication)
                        .getId();

        return ResponseEntity.ok(
                vendorRequestService
                        .getRequestByUser(userId)
        );
    }
}