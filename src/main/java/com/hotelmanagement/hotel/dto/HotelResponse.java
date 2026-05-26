package com.hotelmanagement.hotel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HotelResponse {
    private Long id;
    private String name;
    private String location;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
