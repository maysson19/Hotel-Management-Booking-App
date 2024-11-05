package com.phegondev.PhegonHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phegondev.PhegonHotel.entity.Booking;
import com.phegondev.PhegonHotel.entity.Room;
import com.phegondev.PhegonHotel.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    private Long id;
    private LocalDate checkInData;
    private LocalDate checkOutData;
    private int numOfAdults;
    private int numOfChildren;
    private int totalNumOfGuest;
    private  String bookingConfirmationCode;
    private User user;
    private Room room;
    public static BookingDTO fromEntity(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCheckInData(booking.getCheckInData());
        dto.setCheckOutData(booking.getCheckOutData());
        dto.setNumOfAdults(booking.getNumOfAdults());
        dto.setNumOfChildren(booking.getNumOfChildren());
        dto.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        dto.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        dto.setUser(booking.getUser());  // Consider mapping only necessary fields
        dto.setRoom(booking.getRoom());  // Consider mapping only necessary fields
        return dto;
    }

}
