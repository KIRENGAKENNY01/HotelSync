package com.hotelmanagement.common.config;

import com.hotelmanagement.hotel.model.entity.Hotel;
import com.hotelmanagement.hotel.model.entity.Room;
import com.hotelmanagement.hotel.model.enums.RoomType;
import com.hotelmanagement.hotel.repository.HotelRepository;
import com.hotelmanagement.hotel.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner initDatabase(HotelRepository hotelRepository, RoomRepository roomRepository) {
        return args -> {
            if (hotelRepository.count() == 0) {
                // Create Hotels
                Hotel hotel1 = Hotel.builder()
                        .name("Grand Plaza Hotel")
                        .location("New York City, NY")
                        .description("A luxurious hotel located in the heart of NYC.")
                        .build();

                Hotel hotel2 = Hotel.builder()
                        .name("Oceanview Resort")
                        .location("Miami, FL")
                        .description("Beautiful beachfront resort with stunning ocean views.")
                        .build();

                Hotel hotel3 = Hotel.builder()
                        .name("Mountain Retreat")
                        .location("Denver, CO")
                        .description("Cozy retreat nestled in the snowy mountains.")
                        .build();

                hotelRepository.saveAll(Arrays.asList(hotel1, hotel2, hotel3));

                // Create Rooms for Hotel 1
                Room room1 = Room.builder().roomNumber("101").roomType(RoomType.SINGLE).pricePerNight(new BigDecimal("100.00")).capacity(1).isAvailable(true).hotel(hotel1).build();
                Room room2 = Room.builder().roomNumber("102").roomType(RoomType.DOUBLE).pricePerNight(new BigDecimal("150.00")).capacity(2).isAvailable(true).hotel(hotel1).build();
                Room room3 = Room.builder().roomNumber("103").roomType(RoomType.SUITE).pricePerNight(new BigDecimal("250.00")).capacity(4).isAvailable(true).hotel(hotel1).build();

                // Create Rooms for Hotel 2
                Room h2Room1 = Room.builder().roomNumber("A1").roomType(RoomType.DOUBLE).pricePerNight(new BigDecimal("300.00")).capacity(2).isAvailable(true).hotel(hotel2).build();
                Room room5 = Room.builder().roomNumber("202").roomType(RoomType.DELUXE).pricePerNight(new BigDecimal("200.00")).capacity(2).isAvailable(true).hotel(hotel2).build();

                // Create Rooms for Hotel 3
                Room h3Room1 = Room.builder().roomNumber("10").roomType(RoomType.SINGLE).pricePerNight(new BigDecimal("120.00")).capacity(1).isAvailable(true).hotel(hotel3).build();
                Room h3Room2 = Room.builder().roomNumber("11").roomType(RoomType.SUITE).pricePerNight(new BigDecimal("350.00")).capacity(4).isAvailable(true).hotel(hotel3).build();

                roomRepository.saveAll(Arrays.asList(room1, room2, room3, h2Room1, room5, h3Room1, h3Room2));

                System.out.println("Database seeded successfully with test Hotels and Rooms!");
            }
        };
    }
}
