package com.hotelmanagement.booking.controller;

import com.hotelmanagement.booking.dto.BookingResponse;
import com.hotelmanagement.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@Tag(name = "9. Admin - Booking Management", description = "Endpoints for Admin to oversee bookings")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(
        summary = "Get all bookings",
        description = "Returns all bookings in the system. Look for **PENDING** ones that need confirmation."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @Operation(
        summary = "Confirm a booking",
        description = "Confirms a PENDING booking → status becomes **CONFIRMED**. Generates an invoice and emails it to the customer for payment."
    )
    @Parameters({
        @Parameter(name = "id", in = ParameterIn.PATH, description = "Booking ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @Operation(
        summary = "Cancel a booking",
        description = "Cancels any booking regardless of status. Frees the room if it was confirmed and paid. Sends a cancellation email to the customer."
    )
    @Parameters({
        @Parameter(name = "id", in = ParameterIn.PATH, description = "Booking ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelAnyBooking(id);
        return ResponseEntity.noContent().build();
    }
}
