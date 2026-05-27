package com.hotelmanagement.billing.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BillingResponse {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;  // Same as issuedAt for Option A (immediate payment)
}
