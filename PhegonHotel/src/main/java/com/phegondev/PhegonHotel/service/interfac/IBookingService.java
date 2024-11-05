package com.phegondev.PhegonHotel.service.interfac;
import com.phegondev.PhegonHotel.dto.Response;
import com.phegondev.PhegonHotel.entity.Booking;
import org.springframework.stereotype.Service;

@Service
public interface IBookingService {
    Response saveBooking(Long roomId , Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
