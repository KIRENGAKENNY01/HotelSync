package com.hotelmanagement.hotel.service.impl;

import com.hotelmanagement.auth.repository.UserRepository;
import com.hotelmanagement.billing.model.entity.Billing;
import com.hotelmanagement.billing.repository.BillingRepository;
import com.hotelmanagement.booking.repository.BookingRepository;
import com.hotelmanagement.hotel.repository.HotelRepository;
import com.hotelmanagement.hotel.repository.RoomRepository;
import com.hotelmanagement.hotel.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getDashboardStats() {
        long totalHotels = hotelRepository.count();
        long totalRooms = roomRepository.count();
        long totalBookings = bookingRepository.count();
        long totalUsers = userRepository.count();

        BigDecimal totalRevenue = billingRepository.findAll().stream()
                .map(Billing::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalHotels", totalHotels);
        stats.put("totalRooms", totalRooms);
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalCustomers", totalUsers); // Considering all users as customers for stats
        
        return stats;
    }
}
