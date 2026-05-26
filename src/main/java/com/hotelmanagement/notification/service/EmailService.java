package com.hotelmanagement.notification.service;

import com.hotelmanagement.booking.model.entity.Booking;

public interface EmailService {
    void sendBookingConfirmation(Booking booking);
    void sendBookingCancellation(Booking booking);
}
