package com.hotelmanagement.billing.service;

import com.hotelmanagement.billing.dto.BillingResponse;
import com.hotelmanagement.booking.model.entity.Booking;

public interface BillingService {
    void generateBill(Booking booking);
    BillingResponse getBillByBookingId(Long bookingId);
}
