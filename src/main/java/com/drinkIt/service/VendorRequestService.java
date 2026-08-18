package com.drinkIt.service;

import java.util.List;

import com.drinkIt.dto.vendor.VendorRequestResponse;

public interface VendorRequestService {

    VendorRequestResponse apply(
            Long userId,
            VendorRequestResponse request
    );

    List<VendorRequestResponse> getPendingRequests();

    VendorRequestResponse getRequest(Long requestId);

    VendorRequestResponse approve(Long requestId);

    VendorRequestResponse reject(
            Long requestId,
            String reason
    );

    VendorRequestResponse getRequestByUser(Long userId);
}