package com.phegondev.PhegonHotel.repo;

import com.phegondev.PhegonHotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByRoomId(Long roomId);

    Optional<Booking> findByBookingConfirmationCode(String ConfirmationCode);

    List<Booking> findByUserId(Long userId);
}