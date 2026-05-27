package com.hotelmanagement.billing.controller;

import com.hotelmanagement.billing.dto.BillingResponse;
import com.hotelmanagement.billing.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/billing")
@Tag(name = "10. Admin - Billing Management", description = "Endpoints for Admin to oversee billing and invoices")
public class AdminBillingController {

    @Autowired
    private BillingService billingService;

    @Operation(summary = "Get all billing records")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BillingResponse>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }
}
