package com.hotelmanagement.hotel.service.impl;

import com.hotelmanagement.common.exception.ResourceNotFoundException;
import com.hotelmanagement.hotel.dto.HotelRequest;
import com.hotelmanagement.hotel.dto.HotelResponse;
import com.hotelmanagement.hotel.model.entity.Hotel;
import com.hotelmanagement.hotel.repository.HotelRepository;
import com.hotelmanagement.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public HotelResponse createHotel(HotelRequest hotelRequest) {
        Hotel hotel = Hotel.builder()
                .name(hotelRequest.getName())
                .location(hotelRequest.getLocation())
                .description(hotelRequest.getDescription())
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);
        return mapToResponse(savedHotel);
    }

    @Override
    public HotelResponse updateHotel(Long id, HotelRequest hotelRequest) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        hotel.setName(hotelRequest.getName());
        hotel.setLocation(hotelRequest.getLocation());
        hotel.setDescription(hotelRequest.getDescription());

        Hotel updatedHotel = hotelRepository.save(hotel);
        return mapToResponse(updatedHotel);
    }

    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        hotelRepository.delete(hotel);
    }

    @Override
    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return mapToResponse(hotel);
    }

    @Override
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private HotelResponse mapToResponse(Hotel hotel) {
        return HotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .location(hotel.getLocation())
                .description(hotel.getDescription())
                .createdAt(hotel.getCreatedAt())
                .updatedAt(hotel.getUpdatedAt())
                .build();
    }
}
