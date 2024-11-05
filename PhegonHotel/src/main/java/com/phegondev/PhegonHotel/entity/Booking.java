package com.phegondev.PhegonHotel.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.awt.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "check in data is required")
    private LocalDate checkInData;
    @Future(message = "check out data must be in the future")
    private LocalDate checkOutData;

    @Min(value = 1,message = "Number of adults must not be less that 1")
    private int numOfAdults;

    @Min(value = 0,message = "Number of children must not be less that 0")
    private int numOfChildren;
    private int totalNumOfGuest;
    private  String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalNumberOfGuest(){
        this.totalNumOfGuest=this.numOfAdults + this.numOfChildren;
    }


    public void setNumOfChildren( int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfAdults( int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public @Future(message = "check out data must be in the future") LocalDate getCheckOutData() {
        return checkOutData;
    }

    public void setCheckOutData(@Future(message = "check out data must be in the future") LocalDate checkOutData) {
        this.checkOutData = checkOutData;
    }

    public @NotNull(message = "check in data is required") LocalDate getCheckInData() {
        return checkInData;
    }

    public void setCheckInData(@NotNull(message = "check in data is required") LocalDate checkInData) {
        this.checkInData = checkInData;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getBookingConfirmationCode() {
        return bookingConfirmationCode;
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkInData=" + checkInData +
                ", checkOutData=" + checkOutData +
                ", numOfAdults=" + numOfAdults +
                ", numOfChildren=" + numOfChildren +
                ", totalNumOfGuest=" + totalNumOfGuest +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +

                '}';
    }
}
