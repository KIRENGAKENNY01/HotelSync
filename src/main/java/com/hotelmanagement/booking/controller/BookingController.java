package com.hotelmanagement.booking.controller;

import com.hotelmanagement.booking.dto.BookingRequest;
import com.hotelmanagement.booking.dto.BookingResponse;
import com.hotelmanagement.booking.service.BookingService;
import com.hotelmanagement.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "Create a new booking")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(bookingService.createBooking(userDetails.getId(), bookingRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get my bookings")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.getUserBookings(userDetails.getId()));
    }

    @Operation(summary = "Cancel a booking by ID")
    @Parameters({
        @Parameter(name = "id", in = ParameterIn.PATH, description = "Booking ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        bookingService.cancelBooking(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
