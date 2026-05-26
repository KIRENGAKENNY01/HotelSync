package com.hotelmanagement.booking.repository;

import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.booking.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
           "AND b.status != 'CANCELLED' " +
           "AND ((b.checkInDate <= :checkOutDate) AND (b.checkOutDate >= :checkInDate))")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId, 
                                          @Param("checkInDate") LocalDate checkInDate, 
                                          @Param("checkOutDate") LocalDate checkOutDate);
}
