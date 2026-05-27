package com.hotelmanagement.billing.service;

import com.hotelmanagement.billing.dto.BillingResponse;
import com.hotelmanagement.booking.model.entity.Booking;

import com.hotelmanagement.billing.model.entity.Billing;

public interface BillingService {
    Billing generateBill(Booking booking);
    BillingResponse payBill(Long billingId, Long userId);
    BillingResponse getBillByBookingId(Long bookingId);
    java.util.List<BillingResponse> getAllBills();
    java.util.List<BillingResponse> getBillsByUserId(Long userId);
}
