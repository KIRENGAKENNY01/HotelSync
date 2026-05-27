package com.hotelmanagement.hotel.controller;

import com.hotelmanagement.hotel.dto.HotelRequest;
import com.hotelmanagement.hotel.dto.HotelResponse;
import com.hotelmanagement.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<HotelResponse> updateHotel(@Parameter(in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1") @PathVariable Long id, @Valid @RequestBody HotelRequest hotelRequest) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotelRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@Parameter(in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1") @PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@Parameter(in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
}
