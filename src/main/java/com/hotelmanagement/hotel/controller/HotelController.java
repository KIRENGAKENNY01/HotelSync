package com.hotelmanagement.hotel.controller;

import com.hotelmanagement.hotel.dto.HotelRequest;
import com.hotelmanagement.hotel.dto.HotelResponse;
import com.hotelmanagement.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody HotelRequest hotelRequest) {
        return new ResponseEntity<>(hotelService.createHotel(hotelRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelRequest hotelRequest) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotelRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
}
