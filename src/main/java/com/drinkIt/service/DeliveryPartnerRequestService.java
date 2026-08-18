package com.drinkIt.service;

import java.util.List;

import com.drinkIt.dto.delivery.DeliveryPartnerRequestResponse;

public interface DeliveryPartnerRequestService {

    DeliveryPartnerRequestResponse apply(
            Long userId,
            DeliveryPartnerRequestResponse request
    );

    List<DeliveryPartnerRequestResponse>
    getPendingRequests();

    DeliveryPartnerRequestResponse getRequest(
            Long requestId
    );

    DeliveryPartnerRequestResponse approve(
            Long requestId
    );

    DeliveryPartnerRequestResponse reject(
            Long requestId,
            String reason
    );

    DeliveryPartnerRequestResponse getRequestByUser(
        Long userId
    );
}