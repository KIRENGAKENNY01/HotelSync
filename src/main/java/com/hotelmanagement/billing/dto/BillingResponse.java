package com.hotelmanagement.billing.dto;

import com.hotelmanagement.billing.model.enums.BillingStatus;
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
    private BillingStatus status;
    private LocalDateTime issuedAt;
}
