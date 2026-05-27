package com.hotelmanagement.hotel.dto;

import com.hotelmanagement.hotel.model.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRequest {

    @NotBlank
    private String roomNumber;

    @NotNull
    @Schema(description = "Room type. Capacity is auto-set: SINGLE=1, DOUBLE=2, DELUXE=4, SUITE=2")
    private RoomType roomType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerNight;

    @Schema(description = "Optional. If not provided, defaults to the room type's suggested capacity.")
    private Integer capacity;

    private Boolean isAvailable = true;
}
