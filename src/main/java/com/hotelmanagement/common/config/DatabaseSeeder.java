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
                Room h1Room1 = Room.builder().roomNumber("101").roomType(RoomType.SINGLE).price(new BigDecimal("150.00")).isAvailable(true).hotel(hotel1).build();
                Room h1Room2 = Room.builder().roomNumber("102").roomType(RoomType.DOUBLE).price(new BigDecimal("250.00")).isAvailable(true).hotel(hotel1).build();
                Room h1Room3 = Room.builder().roomNumber("201").roomType(RoomType.SUITE).price(new BigDecimal("500.00")).isAvailable(true).hotel(hotel1).build();

                // Create Rooms for Hotel 2
                Room h2Room1 = Room.builder().roomNumber("A1").roomType(RoomType.DOUBLE).price(new BigDecimal("300.00")).isAvailable(true).hotel(hotel2).build();
                Room h2Room2 = Room.builder().roomNumber("A2").roomType(RoomType.DELUXE).price(new BigDecimal("450.00")).isAvailable(true).hotel(hotel2).build();

                // Create Rooms for Hotel 3
                Room h3Room1 = Room.builder().roomNumber("10").roomType(RoomType.SINGLE).price(new BigDecimal("120.00")).isAvailable(true).hotel(hotel3).build();
                Room h3Room2 = Room.builder().roomNumber("11").roomType(RoomType.SUITE).price(new BigDecimal("350.00")).isAvailable(true).hotel(hotel3).build();

                roomRepository.saveAll(Arrays.asList(h1Room1, h1Room2, h1Room3, h2Room1, h2Room2, h3Room1, h3Room2));

                System.out.println("Database seeded successfully with test Hotels and Rooms!");
            }
        };
    }
}
