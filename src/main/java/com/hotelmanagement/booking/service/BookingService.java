package com.hotelmanagement.booking.service;

import com.hotelmanagement.booking.dto.BookingRequest;
import com.hotelmanagement.booking.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingRequest bookingRequest);
    List<BookingResponse> getUserBookings(Long userId);
    void cancelBooking(Long bookingId, Long userId);
}
