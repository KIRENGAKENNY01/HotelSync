package com.hotelmanagement.billing.controller;

import com.hotelmanagement.billing.dto.BillingResponse;
import com.hotelmanagement.billing.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/{bookingId}")
    public ResponseEntity<BillingResponse> getBillByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(billingService.getBillByBookingId(bookingId));
    }
}
