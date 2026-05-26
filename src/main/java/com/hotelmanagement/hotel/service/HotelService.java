package com.hotelmanagement.hotel.service;

import com.hotelmanagement.hotel.dto.HotelRequest;
import com.hotelmanagement.hotel.dto.HotelResponse;

import java.util.List;

public interface HotelService {
    HotelResponse createHotel(HotelRequest hotelRequest);
    HotelResponse updateHotel(Long id, HotelRequest hotelRequest);
    void deleteHotel(Long id);
    HotelResponse getHotelById(Long id);
    List<HotelResponse> getAllHotels();
}
