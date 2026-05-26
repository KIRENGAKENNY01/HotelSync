package com.hotelmanagement.hotel.dto;

import com.hotelmanagement.hotel.model.enums.RoomType;
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
    private RoomType roomType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    
    private Boolean isAvailable = true;
}
