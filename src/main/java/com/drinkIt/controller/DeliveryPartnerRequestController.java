package com.drinkIt.controller;

import com.drinkIt.dto.delivery.DeliveryPartnerRequestResponse;
import com.drinkIt.security.CurrentUserService;
import com.drinkIt.service.DeliveryPartnerRequestService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery-partner-requests")
@RequiredArgsConstructor
public class DeliveryPartnerRequestController {

    private final DeliveryPartnerRequestService service;

    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<DeliveryPartnerRequestResponse>
    apply(
            Authentication authentication,
            @RequestBody DeliveryPartnerRequestResponse request
    ) {

        Long userId =
                currentUserService
                        .getUser(authentication)
                        .getId();

        return ResponseEntity.ok(
                service.apply(
                        userId,
                        request
                )
        );
    }

    @GetMapping("/my")
    public ResponseEntity<DeliveryPartnerRequestResponse>
    myRequest(
            Authentication authentication
    ) {

        Long userId =
                currentUserService
                        .getUser(authentication)
                        .getId();

        return ResponseEntity.ok(
                service.getRequestByUser(userId)
        );
    }
}