package com.hotelmanagement.booking.controller;

import com.hotelmanagement.booking.dto.BookingRequest;
import com.hotelmanagement.booking.dto.BookingResponse;
import com.hotelmanagement.booking.service.BookingService;
import com.hotelmanagement.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(bookingService.createBooking(userDetails.getId(), bookingRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.getUserBookings(userDetails.getId()));
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        bookingService.cancelBooking(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
