package com.hotelmanagement.notification.service.impl;

import com.hotelmanagement.billing.model.entity.Billing;
import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.notification.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from:${spring.mail.username}}")
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
                + "<p>Your booking at <b>" + booking.getHotel().getName() + "</b> for room "
                + booking.getRoom().getRoomNumber() + " has been successfully cancelled.</p>"
                + "<p>We hope to see you again soon.</p>";
        sendHtmlEmail(to, subject, content);
    }

    @Override
    public void sendBillToCustomer(Booking booking, Billing billing) {
        String to = booking.getUser().getEmail();
        String subject = "Your Invoice - Booking #" + booking.getId() + " - Hotel Management System";
        String content = "<h1>Your Invoice</h1>"
                + "<p>Dear " + booking.getUser().getFullName() + ",</p>"
                + "<p>Your booking at <b>" + booking.getHotel().getName() + "</b> has been confirmed by our team.</p>"
                + "<hr/>"
                + "<h3>Invoice Details</h3>"
                + "<table style='border-collapse:collapse;width:100%'>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Billing ID</b></td><td style='padding:8px;border:1px solid #ddd'>#" + billing.getId() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Booking ID</b></td><td style='padding:8px;border:1px solid #ddd'>#" + booking.getId() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Hotel</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getHotel().getName() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Room</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getRoom().getRoomNumber() + " (" + booking.getRoom().getRoomType() + ")</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Check-in</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getCheckInDate() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Check-out</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getCheckOutDate() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Amount Due</b></td><td style='padding:8px;border:1px solid #ddd;color:green'><b>$" + billing.getAmount() + "</b></td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Payment Status</b></td><td style='padding:8px;border:1px solid #ddd'>" + billing.getPaymentStatus() + "</td></tr>"
                + "</table>"
                + "<br/><p>Please complete your payment to secure your reservation.</p>"
                + "<p>Thank you for choosing our service!</p>";
        sendHtmlEmail(to, subject, content);
    }

    @Override
    public void sendPaymentConfirmation(Booking booking, Billing billing) {
        String to = booking.getUser().getEmail();
        String subject = "Payment Confirmed - Booking #" + booking.getId() + " - Hotel Management System";
        String content = "<h1 style='color:green'>Payment Successful!</h1>"
                + "<p>Dear " + booking.getUser().getFullName() + ",</p>"
                + "<p>Your payment has been received and your room is now secured.</p>"
                + "<hr/>"
                + "<h3>Payment Receipt</h3>"
                + "<table style='border-collapse:collapse;width:100%'>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Booking ID</b></td><td style='padding:8px;border:1px solid #ddd'>#" + booking.getId() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Hotel</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getHotel().getName() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Room</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getRoom().getRoomNumber() + " (" + booking.getRoom().getRoomType() + ")</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Check-in</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getCheckInDate() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Check-out</b></td><td style='padding:8px;border:1px solid #ddd'>" + booking.getCheckOutDate() + "</td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Amount Paid</b></td><td style='padding:8px;border:1px solid #ddd;color:green'><b>$" + billing.getAmount() + "</b></td></tr>"
                + "<tr><td style='padding:8px;border:1px solid #ddd'><b>Payment Status</b></td><td style='padding:8px;border:1px solid #ddd;color:green'><b>PAID</b></td></tr>"
                + "</table>"
                + "<br/><p>Your reservation is confirmed. We look forward to welcoming you!</p>"
                + "<p>Thank you for choosing our service!</p>";
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
        } catch (MessagingException | MailException e) {
            String rootCauseMessage = getRootCauseMessage(e);
            logger.error("Failed to send email to {}: {}", to, rootCauseMessage, e);
            throw new IllegalStateException("Failed to send email to " + to + ": " + rootCauseMessage, e);
        }
    }

    private String getRootCauseMessage(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause.getMessage() != null ? rootCause.getMessage() : throwable.getMessage();
    }
}
