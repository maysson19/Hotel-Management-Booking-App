package com.phegondev.PhegonHotel.service.impl;
import com.phegondev.PhegonHotel.dto.Response;
import com.phegondev.PhegonHotel.dto.RoomDTO;
import com.phegondev.PhegonHotel.entity.Room;
import com.phegondev.PhegonHotel.exception.OurException;
import com.phegondev.PhegonHotel.repo.RoomRepository;
import com.phegondev.PhegonHotel.service.AwsS3Service;
import com.phegondev.PhegonHotel.service.interfac.IRoomService;
import com.phegondev.PhegonHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;
    private AwsS3Service awsS3Service;

    @Override
    public Response addNew(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();

        try{
            String imageUrl = awsS3Service.saveImageToS3(photo);
            Room room=new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(String.valueOf(roomPrice));
            room.setRooDescription(description);
            Room savedRoom= roomRepository.save(room);
            RoomDTO roomDTO= Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);


        }
        catch (OurException e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a room" + e.getMessage());
        }
            return null;
        }
    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try{
            List<Room> roomList= roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<RoomDTO> roomDTOList= Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);

        }
        catch (OurException e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a room" + e.getMessage());
        }
        return response;
    }


    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try{
            roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room Not Found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch (OurException e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a room" + e.getMessage());
        }
        return response;

    }

    @Override
    public Response updateRoom(Long roomId,String description ,String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();

        try{
            String imageUrl= null;
            if(photo != null && !photo.isEmpty()){
                imageUrl= awsS3Service.saveImageToS3(photo);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room Not Saving"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(String.valueOf(roomPrice));
            if (description != null) room.setRooDescription(description);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updateRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updateRoom);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        }catch (OurException e){
            response.setStatusCode(484);
            response.setMessage(e.getMessage());

        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a room" + e.getMessage());
        }
        return response;

    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try{
            Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room Not Saving"));
            RoomDTO roomDTO= Utils.mapRoomEntityToRoomDTOPlusUserBookings(room);
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        }catch (OurException e){
            response.setStatusCode(484);
            response.setMessage(e.getMessage());

        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a room" + e.getMessage());
        }
        return response;

    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try{
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setRoomList(roomDTOList);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a room" + e.getMessage());
        }
        return response;

    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try{
            List<Room> roomList= roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setRoomList(roomDTOList);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a room" + e.getMessage());
        }
        return response;

    }

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String roomDescription) {
        return null;
    }
}
