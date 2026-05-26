package com.hotelmanagement.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {

    @NotNull
    private Long hotelId;

    @NotNull
    private Long roomId;

    @NotNull
    @FutureOrPresent
    private LocalDate checkInDate;

    @NotNull
    @Future
    private LocalDate checkOutDate;
}
