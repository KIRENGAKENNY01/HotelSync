package com.hotelmanagement.hotel.controller;

import com.hotelmanagement.hotel.dto.RoomRequest;
import com.hotelmanagement.hotel.dto.RoomResponse;
import com.hotelmanagement.hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels/{hotelId}/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomResponse> addRoomToHotel(@PathVariable Long hotelId, @Valid @RequestBody RoomRequest roomRequest) {
        return new ResponseEntity<>(roomService.addRoomToHotel(hotelId, roomRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @Valid @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, roomRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }
}
