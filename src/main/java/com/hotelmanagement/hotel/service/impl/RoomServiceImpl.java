package com.hotelmanagement.hotel.service.impl;

import com.hotelmanagement.common.exception.ResourceNotFoundException;
import com.hotelmanagement.hotel.dto.RoomRequest;
import com.hotelmanagement.hotel.dto.RoomResponse;
import com.hotelmanagement.hotel.model.entity.Hotel;
import com.hotelmanagement.hotel.model.entity.Room;
import com.hotelmanagement.hotel.repository.HotelRepository;
import com.hotelmanagement.hotel.repository.RoomRepository;
import com.hotelmanagement.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public RoomResponse addRoomToHotel(Long hotelId, RoomRequest roomRequest) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        Room room = Room.builder()
                .roomNumber(roomRequest.getRoomNumber())
                .roomType(roomRequest.getRoomType())
                .price(roomRequest.getPrice())
                .isAvailable(roomRequest.getIsAvailable() != null ? roomRequest.getIsAvailable() : true)
                .hotel(hotel)
                .build();

        Room savedRoom = roomRepository.save(room);
        return mapToResponse(savedRoom);
    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setRoomType(roomRequest.getRoomType());
        room.setPrice(roomRequest.getPrice());
        if (roomRequest.getIsAvailable() != null) {
            room.setIsAvailable(roomRequest.getIsAvailable());
        }

        Room updatedRoom = roomRepository.save(room);
        return mapToResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        roomRepository.delete(room);
    }

    @Override
    public List<RoomResponse> getRoomsByHotel(Long hotelId) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + hotelId);
        }
        return roomRepository.findByHotelId(hotelId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        return mapToResponse(room);
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .isAvailable(room.getIsAvailable())
                .hotelId(room.getHotel().getId())
                .build();
    }
}
