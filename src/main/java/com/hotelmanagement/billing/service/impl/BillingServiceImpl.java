package com.hotelmanagement.billing.service.impl;

import com.hotelmanagement.billing.dto.BillingResponse;
import com.hotelmanagement.billing.model.entity.Billing;
import com.hotelmanagement.billing.model.enums.PaymentStatus;
import com.hotelmanagement.billing.repository.BillingRepository;
import com.hotelmanagement.billing.service.BillingService;
import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Override
    public void generateBill(Booking booking) {
        Billing billing = Billing.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .status(BillingStatus.UNPAID)
                .build();
        billingRepository.save(billing);
    }

    @Override
    public BillingResponse getBillByBookingId(Long bookingId) {
        Billing billing = billingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found for booking id: " + bookingId));

        return BillingResponse.builder()
                .id(billing.getId())
                .bookingId(billing.getBooking().getId())
                .amount(billing.getAmount())
                .status(billing.getStatus())
                .issuedAt(billing.getCreatedAt())
                .build();
    }
}
