package com.hotelmanagement.notification.service;

import com.hotelmanagement.billing.model.entity.Billing;
import com.hotelmanagement.booking.model.entity.Booking;

public interface EmailService {
    void sendBookingConfirmation(Booking booking);
    void sendBookingCancellation(Booking booking);
    void sendBillToCustomer(Booking booking, Billing billing);
    void sendPaymentConfirmation(Booking booking, Billing billing);
}
