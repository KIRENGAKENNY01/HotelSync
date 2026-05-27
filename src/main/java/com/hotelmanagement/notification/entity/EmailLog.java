package com.hotelmanagement.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_logs")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Valid recipient email is required")
    @NotBlank(message = "Recipient is required")
    @Column(nullable = false)
    private String recipient;

    @NotBlank(message = "Subject is required")
    @Column(nullable = false)
    private String subject;

    @NotNull(message = "Sent time is required")
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;
}
