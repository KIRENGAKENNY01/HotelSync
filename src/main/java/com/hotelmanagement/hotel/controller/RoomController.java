package com.hotelmanagement.hotel.controller;

import com.hotelmanagement.hotel.dto.RoomRequest;
import com.hotelmanagement.hotel.dto.RoomResponse;
import com.hotelmanagement.hotel.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hotels/{hotelId}/rooms")
public class RoomController {

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

    @Operation(summary = "Get all rooms for a hotel")
    @Parameters({
        @Parameter(name = "hotelId", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1")
    })
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    @Operation(summary = "Get a room by ID")
    @Parameters({
        @Parameter(name = "hotelId", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1"),
        @Parameter(name = "roomId", in = ParameterIn.PATH, description = "Room ID", required = true, example = "1")
    })
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }
}
