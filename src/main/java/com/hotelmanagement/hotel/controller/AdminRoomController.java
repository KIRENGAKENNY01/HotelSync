package com.hotelmanagement.hotel.controller;

import com.hotelmanagement.hotel.dto.RoomRequest;
import com.hotelmanagement.hotel.dto.RoomResponse;
import com.hotelmanagement.hotel.service.RoomService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/hotels/{hotelId}/rooms")
@Tag(name = "8. Admin - Room Management", description = "Endpoints for Admin to manage hotel rooms")
public class AdminRoomController {

    @Autowired
    private RoomService roomService;

    @Operation(summary = "Add a room to a hotel")
    @Parameters({
        @Parameter(name = "hotelId", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomResponse> addRoomToHotel(@PathVariable Long hotelId, @Valid @RequestBody RoomRequest roomRequest) {
        return new ResponseEntity<>(roomService.addRoomToHotel(hotelId, roomRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a room by ID")
    @Parameters({
        @Parameter(name = "hotelId", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1"),
        @Parameter(name = "roomId", in = ParameterIn.PATH, description = "Room ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @Valid @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, roomRequest));
    }

    @Operation(summary = "Delete a room by ID")
    @Parameters({
        @Parameter(name = "hotelId", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1"),
        @Parameter(name = "roomId", in = ParameterIn.PATH, description = "Room ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
