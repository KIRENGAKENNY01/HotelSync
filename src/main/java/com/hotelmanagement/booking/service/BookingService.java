package com.hotelmanagement.booking.service;

import com.hotelmanagement.booking.dto.BookingRequest;
import com.hotelmanagement.booking.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingRequest bookingRequest);
    BookingResponse confirmBooking(Long bookingId);
    List<BookingResponse> getUserBookings(Long userId);
    List<BookingResponse> getAllBookings();
    void cancelBooking(Long bookingId, Long userId);
    void cancelAnyBooking(Long bookingId);
}
