package com.hotelmanagement.billing.controller;

import com.hotelmanagement.billing.dto.BillingResponse;
import com.hotelmanagement.billing.service.BillingService;
import com.hotelmanagement.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "5. Customer - Billing Management", description = "Endpoints for Customers to view and pay their invoices")
public class CustomerBillingController {

    @Autowired
    private BillingService billingService;

    @Operation(
        summary = "Get my invoices",
        description = "Returns all invoices for the logged-in customer. Check the **status** field: PENDING = unpaid, PAID = payment complete."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my-invoices")
    public ResponseEntity<List<BillingResponse>> getMyInvoices(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(billingService.getBillsByUserId(userDetails.getId()));
    }

    @Operation(
        summary = "Pay a bill",
        description = "Pays an invoice by its ID. Marks the bill as **PAID**, locks the room, and sends a payment confirmation email. You can only pay your own bills."
    )
    @Parameters({
        @Parameter(name = "id", in = ParameterIn.PATH, description = "Bill ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{id}/pay")
    public ResponseEntity<BillingResponse> payBill(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(billingService.payBill(id, userDetails.getId()));
    }
}
