package com.hotelmanagement.hotel.service;

import com.hotelmanagement.hotel.dto.RoomRequest;
import com.hotelmanagement.hotel.dto.RoomResponse;

import java.util.List;

public interface RoomService {
    RoomResponse addRoomToHotel(Long hotelId, RoomRequest roomRequest);
    RoomResponse updateRoom(Long roomId, RoomRequest roomRequest);
    void deleteRoom(Long roomId);
    List<RoomResponse> getRoomsByHotel(Long hotelId);
    RoomResponse getRoomById(Long roomId);
}
