package com.hotelmanagement.notification.service.impl;

import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.notification.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendBookingConfirmation(Booking booking) {
        String to = booking.getUser().getEmail();
        String subject = "Booking Confirmation - Hotel Management System";
        String content = "<h1>Booking Confirmed!</h1>"
                + "<p>Dear " + booking.getUser().getFullName() + ",</p>"
                + "<p>Your booking at <b>" + booking.getHotel().getName() + "</b> has been confirmed.</p>"
                + "<p><b>Room:</b> " + booking.getRoom().getRoomNumber() + " (" + booking.getRoom().getRoomType() + ")</p>"
                + "<p><b>Check-in:</b> " + booking.getCheckInDate() + "</p>"
                + "<p><b>Check-out:</b> " + booking.getCheckOutDate() + "</p>"
                + "<p><b>Total Price:</b> $" + booking.getTotalPrice() + "</p>"
                + "<p>Thank you for choosing our service!</p>";

        sendHtmlEmail(to, subject, content);
    }

    @Override
    public void sendBookingCancellation(Booking booking) {
        String to = booking.getUser().getEmail();
        String subject = "Booking Cancellation - Hotel Management System";
        String content = "<h1>Booking Cancelled</h1>"
                + "<p>Dear " + booking.getUser().getFullName() + ",</p>"
                + "<p>Your booking at <b>" + booking.getHotel().getName() + "</b> for room " + booking.getRoom().getRoomNumber() + " has been successfully cancelled.</p>"
                + "<p>We hope to see you again soon.</p>";

        sendHtmlEmail(to, subject, content);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Sent email to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
