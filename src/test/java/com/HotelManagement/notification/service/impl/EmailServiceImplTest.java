package com.hotelmanagement.notification.service.impl;

import com.hotelmanagement.auth.model.entity.User;
import com.hotelmanagement.billing.model.entity.Billing;
import com.hotelmanagement.billing.model.enums.PaymentStatus;
import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.booking.model.enums.BookingStatus;
import com.hotelmanagement.hotel.model.entity.Hotel;
import com.hotelmanagement.hotel.model.entity.Room;
import com.hotelmanagement.hotel.model.enums.RoomType;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private MimeMessage message;

    @BeforeEach
    void setUp() {
        message = new MimeMessage(Session.getInstance(new Properties()));
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@hotel.test");
    }

    @Test
    void sendBillToCustomerSendsEmail() {
        when(mailSender.createMimeMessage()).thenReturn(message);

        emailService.sendBillToCustomer(booking(), billing());

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendBillToCustomerFailsWhenMailCannotBeSent() {
        when(mailSender.createMimeMessage()).thenReturn(message);
        doThrow(new MailSendException("SMTP unavailable"))
                .when(mailSender)
                .send(any(MimeMessage.class));

        assertThrows(IllegalStateException.class,
                () -> emailService.sendBillToCustomer(booking(), billing()));
    }

    private Booking booking() {
        User user = User.builder()
                .fullName("Jane Guest")
                .email("jane@example.com")
                .password("secret")
                .build();
        Hotel hotel = Hotel.builder()
                .name("Lake View Hotel")
                .location("Kigali")
                .build();
        Room room = Room.builder()
                .roomNumber("101")
                .roomType(RoomType.SINGLE)
                .pricePerNight(BigDecimal.valueOf(100))
                .capacity(1)
                .hotel(hotel)
                .build();

        return Booking.builder()
                .id(10L)
                .user(user)
                .hotel(hotel)
                .room(room)
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3))
                .totalPrice(BigDecimal.valueOf(200))
                .status(BookingStatus.CONFIRMED)
                .build();
    }

    private Billing billing() {
        return Billing.builder()
                .amount(BigDecimal.valueOf(200))
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }
}
