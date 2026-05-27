package com.hotelmanagement.booking.controller;

import com.hotelmanagement.booking.dto.BookingRequest;
import com.hotelmanagement.booking.dto.BookingResponse;
import com.hotelmanagement.booking.service.BookingService;
import com.hotelmanagement.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "4. Customer - Booking Management", description = "Endpoints for Customers to manage their bookings")
public class CustomerBookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(
        summary = "Create a new booking",
        description = "Submits a booking request. Status starts as **PENDING** — awaiting admin confirmation. No payment is taken yet."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(bookingService.createBooking(userDetails.getId(), bookingRequest), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Get my bookings",
        description = "Returns all bookings for the logged-in customer with their current status (PENDING / CONFIRMED / CANCELLED)."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.getUserBookings(userDetails.getId()));
    }

    @Operation(
        summary = "Cancel my booking",
        description = "Cancels a booking. If the booking was already CONFIRMED and paid, the room is freed. A cancellation email is sent."
    )
    @Parameters({
        @Parameter(name = "id", in = ParameterIn.PATH, description = "Booking ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        bookingService.cancelBooking(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
