package com.phegondev.PhegonHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phegondev.PhegonHotel.entity.Booking;
import com.phegondev.PhegonHotel.entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends User {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingDTO> bookingss = new ArrayList<>();

    // New method specifically for DTOs
    public List<BookingDTO> getBookingDTOs() {
        return super.getBookings().stream()
                .map(this::convertToBookingDTO) // Convert each Booking to BookingDTO
                .collect(Collectors.toList());
    }

    // Conversion method from Booking to BookingDTO
    private BookingDTO convertToBookingDTO(Booking booking) {
        // Assuming BookingDTO has a constructor or method that takes a Booking entity
        return BookingDTO.fromEntity(booking);
    }
}
