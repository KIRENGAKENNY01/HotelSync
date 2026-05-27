package com.hotelmanagement.billing.service.impl;

import com.hotelmanagement.billing.dto.BillingResponse;
import com.hotelmanagement.billing.model.entity.Billing;
import com.hotelmanagement.billing.model.enums.PaymentStatus;
import com.hotelmanagement.billing.repository.BillingRepository;
import com.hotelmanagement.billing.service.BillingService;
import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.common.exception.BadRequestException;
import com.hotelmanagement.common.exception.ResourceNotFoundException;
import com.hotelmanagement.hotel.model.entity.Room;
import com.hotelmanagement.hotel.repository.RoomRepository;
import com.hotelmanagement.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Billing generateBill(Booking booking) {
        Billing billing = Billing.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        return billingRepository.save(billing);
    }

    @Override
    @Transactional
    public BillingResponse payBill(Long billingId, Long userId) {
        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billingId));

        // Verify the bill belongs to the requesting customer
        if (!billing.getBooking().getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only pay your own bills");
        }

        if (billing.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BadRequestException("This bill has already been paid");
        }

        if (billing.getPaymentStatus() == PaymentStatus.FAILED) {
            throw new BadRequestException("This bill is in a failed state and cannot be paid");
        }

        // Mark bill as PAID
        billing.setPaymentStatus(PaymentStatus.PAID);
        Billing paidBilling = billingRepository.save(billing);

        // Lock the room — payment secures the reservation
        Room room = billing.getBooking().getRoom();
        room.setIsAvailable(false);
        roomRepository.save(room);

        // Send payment confirmation email
        emailService.sendPaymentConfirmation(billing.getBooking(), paidBilling);

        return mapToResponse(paidBilling);
    }

    @Override
    public BillingResponse getBillByBookingId(Long bookingId) {
        Billing billing = billingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found for booking id: " + bookingId));
        return mapToResponse(billing);
    }

    @Override
    public List<BillingResponse> getAllBills() {
        return billingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillingResponse> getBillsByUserId(Long userId) {
        return billingRepository.findAll().stream()
                .filter(b -> b.getBooking().getUser().getId().equals(userId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BillingResponse mapToResponse(Billing billing) {
        return BillingResponse.builder()
                .id(billing.getId())
                .bookingId(billing.getBooking().getId())
                .amount(billing.getAmount())
                .status(billing.getPaymentStatus().name())
                .issuedAt(billing.getCreatedAt())
                .paidAt(billing.getPaymentStatus() == PaymentStatus.PAID ? billing.getUpdatedAt() : null)
                .build();
    }
}
