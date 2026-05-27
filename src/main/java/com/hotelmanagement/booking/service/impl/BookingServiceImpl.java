package com.hotelmanagement.booking.service.impl;

import com.hotelmanagement.auth.model.entity.User;
import com.hotelmanagement.auth.repository.UserRepository;
import com.hotelmanagement.billing.model.entity.Billing;
import com.hotelmanagement.billing.service.BillingService;
import com.hotelmanagement.booking.dto.BookingRequest;
import com.hotelmanagement.booking.dto.BookingResponse;
import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.booking.model.enums.BookingStatus;
import com.hotelmanagement.booking.repository.BookingRepository;
import com.hotelmanagement.booking.service.BookingService;
import com.hotelmanagement.common.exception.BadRequestException;
import com.hotelmanagement.common.exception.ResourceNotFoundException;
import com.hotelmanagement.hotel.model.entity.Hotel;
import com.hotelmanagement.hotel.model.entity.Room;
import com.hotelmanagement.hotel.repository.HotelRepository;
import com.hotelmanagement.hotel.repository.RoomRepository;
import com.hotelmanagement.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BillingService billingService;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public BookingResponse createBooking(Long userId, BookingRequest bookingRequest) {
        if (bookingRequest.getCheckInDate().isAfter(bookingRequest.getCheckOutDate()) ||
            bookingRequest.getCheckInDate().isEqual(bookingRequest.getCheckOutDate())) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (!room.getHotel().getId().equals(hotel.getId())) {
            throw new BadRequestException("Room does not belong to the specified hotel");
        }

        if (!room.getIsAvailable()) {
            throw new BadRequestException("Room is not available for booking");
        }

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        if (!overlappingBookings.isEmpty()) {
            throw new BadRequestException("Room is already booked for the selected dates");
        }

        long days = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(days));

        // Save as PENDING — awaiting admin confirmation
        Booking booking = Booking.builder()
                .user(user)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponse confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new BadRequestException("Booking is already confirmed");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot confirm a cancelled booking");
        }

        // Confirm the booking
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking confirmedBooking = bookingRepository.save(booking);

        // Generate bill (PENDING — customer still needs to pay)
        Billing billing = billingService.generateBill(confirmedBooking);

        // Send invoice email to customer — they will pay to lock the room
        emailService.sendBillToCustomer(confirmedBooking, billing);

        return mapToResponse(confirmedBooking);
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only cancel your own bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        boolean wasConfirmed = booking.getStatus() == BookingStatus.CONFIRMED;

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Free the room only if it was confirmed (room was locked after payment)
        if (wasConfirmed) {
            Room room = booking.getRoom();
            room.setIsAvailable(true);
            roomRepository.save(room);
        }

        emailService.sendBookingCancellation(booking);
    }

    @Override
    @Transactional
    public void cancelAnyBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        boolean wasConfirmed = booking.getStatus() == BookingStatus.CONFIRMED;

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Free the room if it was previously confirmed (room was locked)
        if (wasConfirmed) {
            Room room = booking.getRoom();
            room.setIsAvailable(true);
            roomRepository.save(room);
        }

        // Send cancellation email to customer
        emailService.sendBookingCancellation(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .hotelId(booking.getHotel().getId())
                .roomId(booking.getRoom().getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .build();
    }
}
