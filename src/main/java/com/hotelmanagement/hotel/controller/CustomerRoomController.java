package com.hotelmanagement.hotel.controller;

import com.hotelmanagement.hotel.dto.RoomResponse;
import com.hotelmanagement.hotel.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels/{hotelId}/rooms")
@Tag(name = "3. Customer - Room Management", description = "Endpoints for Customers to view hotel rooms")
public class CustomerRoomController {

    @Autowired
    private RoomService roomService;

    @Operation(summary = "Get all rooms for a hotel")
    @Parameters({
        @Parameter(name = "hotelId", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    @Operation(summary = "Get a room by ID")
    @Parameters({
        @Parameter(name = "hotelId", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1"),
        @Parameter(name = "roomId", in = ParameterIn.PATH, description = "Room ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }
}
