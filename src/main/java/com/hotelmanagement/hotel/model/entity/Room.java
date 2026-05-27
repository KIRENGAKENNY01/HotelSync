package com.hotelmanagement.hotel.model.entity;

import com.hotelmanagement.booking.model.entity.Booking;
import com.hotelmanagement.common.entity.BaseEntity;
import com.hotelmanagement.hotel.model.enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"hotel", "bookings"})
@ToString(exclude = {"hotel", "bookings"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room number is required")
    @Column(nullable = false, unique = true)
    private String roomNumber;

    @NotNull(message = "Room type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @NotNull(message = "Price per night is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
}
