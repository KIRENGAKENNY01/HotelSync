package com.hotelmanagement.billing.repository;

import com.hotelmanagement.billing.model.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByBookingId(Long bookingId);
}
