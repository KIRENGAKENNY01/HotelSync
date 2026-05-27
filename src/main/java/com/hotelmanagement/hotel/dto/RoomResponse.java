package com.hotelmanagement.hotel.dto;

import com.hotelmanagement.hotel.model.enums.RoomType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private Boolean isAvailable;
    private Long hotelId;
}
