package com.phegondev.PhegonHotel.service.impl;
import com.phegondev.PhegonHotel.dto.BookingDTO;
import com.phegondev.PhegonHotel.dto.Response;
import com.phegondev.PhegonHotel.entity.Booking;
import com.phegondev.PhegonHotel.entity.Room;
import com.phegondev.PhegonHotel.entity.User;
import com.phegondev.PhegonHotel.exception.OurException;
import com.phegondev.PhegonHotel.repo.BookingRepository;
import com.phegondev.PhegonHotel.repo.RoomRepository;
import com.phegondev.PhegonHotel.repo.UserRepository;
import com.phegondev.PhegonHotel.service.interfac.IBookingService;
import com.phegondev.PhegonHotel.service.interfac.IRoomService;
import com.phegondev.PhegonHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {

        Response response = new Response();
         try {

             if(bookingRequest.getCheckOutData().isBefore(bookingRequest.getCheckInData())){
                 throw new IllegalArgumentException("Check in date must come after check out date");
             }
             Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room Not Found"));
             User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User Not Found"));

             List<Booking> existingBookings = room.getBookings();
             if(!roomIsAvailable(bookingRequest,existingBookings)){
                 throw new OurException("Room not Available for selected date range");

             }
             bookingRequest.setRoom(room);
             bookingRequest.setUser(user);
             String bookingConfirmationCode = Utils.generateRandomAlphanumeric(10);
             bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
             bookingRepository.save(bookingRequest);
             response.setStatusCode(200);
             response.setMessage("Successful");
             response.setBookingConfirmationCode(bookingConfirmationCode);

         }catch (OurException e){
             response.setStatusCode(484);
             response.setMessage(e.getMessage());

         }catch (Exception e){
             response.setStatusCode(500);
             response.setMessage("Error Saving a booking"+e.getMessage());

         }

        return response;
    }


    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
           return true;

    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try {

           Booking booking= bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("Booking Not Found"));
            BookingDTO bookingDTO= Utils.mapBookingEntityToBookingDTO(booking);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingConfirmationCode(confirmationCode);

        }catch (OurException e){
            response.setStatusCode(484);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Saving a booking"+e.getMessage());

        }

        return response;
    }

    @Override
    public Response getAllBookings() {

        Response response = new Response();
        try {
            List<Booking> bookingList= bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<BookingDTO> bookingDTOList= Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingList(bookingDTOList);

        }catch (OurException e){
            response.setStatusCode(484);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Saving a booking"+e.getMessage());

        }

        return response;
    }
    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try {
            bookingRepository.findById(bookingId).orElseThrow(()-> new OurException("Booking Dose Not Exists"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch (OurException e){
            response.setStatusCode(484);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a booking"+e.getMessage());

        }

        return response;
    }
}
