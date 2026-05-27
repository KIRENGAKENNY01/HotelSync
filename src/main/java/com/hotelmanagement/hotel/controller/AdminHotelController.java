package com.hotelmanagement.hotel.controller;

import com.hotelmanagement.hotel.dto.HotelRequest;
import com.hotelmanagement.hotel.dto.HotelResponse;
import com.hotelmanagement.hotel.service.HotelService;
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
@RequestMapping("/api/admin/hotels")
@Tag(name = "7. Admin - Hotel Management", description = "Endpoints for Admin to manage hotels")
public class AdminHotelController {

    @Autowired
    private HotelService hotelService;

    @Operation(summary = "Add a new hotel")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody HotelRequest hotelRequest) {
        return new ResponseEntity<>(hotelService.createHotel(hotelRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update hotel details")
    @Parameters({
        @Parameter(name = "id", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelRequest hotelRequest) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotelRequest));
    }

    @Operation(summary = "Delete a hotel")
    @Parameters({
        @Parameter(name = "id", in = ParameterIn.PATH, description = "Hotel ID", required = true, example = "1")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "View all hotels")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
}
