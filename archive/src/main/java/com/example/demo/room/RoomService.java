package com.example.demo.room;

import com.example.demo.reservation.Reservation;
import com.example.demo.utils.DateHandler;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;


    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomByName(String roomName){
        Optional<Room> roomByName = roomRepository.findByName(roomName);
        if(roomByName.isEmpty()){
            throw new IllegalStateException("No such room");
        }
        return roomByName.get();
    }

    public List<StartEnd> getRoomsTimeSlots(String roomName){
        Room room = getRoomByName(roomName);
        LocalTime start = room.getStartTime().toLocalTime();
        LocalTime end = room.getEndTime().toLocalTime();
        int minutes = room.getReservationTimeInMinutes();
        ArrayList<StartEnd> slots = new ArrayList<StartEnd>();
        while(start.compareTo(end.plusMinutes(-minutes)) <= 0 ){
            StartEnd startEnd = new StartEnd();
            startEnd.setStart(start.toString());
            start = start.plusMinutes(minutes);
            startEnd.setEnd(start.toString());
            slots.add(startEnd);
        }
        return slots;
    }

    public List<StartEnd> getRoomsTimeSlotsForDate(String roomName, String date){
        List<StartEnd> allSlots = getRoomsTimeSlots(roomName);
        List<Reservation> reservations = getRoomReservations(roomName);
        for (StartEnd slot: allSlots){
            String startDateString = date + " " + slot.getStart() +":00";
            String endDateString = date + " " + slot.getEnd() +":00";

            Timestamp startDate = new Timestamp(DateHandler.handleDate(startDateString).getTime());
            Timestamp endDate = new Timestamp(DateHandler.handleDate(endDateString).getTime());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            slot.setReserved(isSlotReserved(reservations, startDate, endDate, now));
            slot.setDate(date);
        }
        return allSlots;
    }

    public List<StartEnd> getRoomsTimeSlotsForThreeDays(String roomName){
        List<StartEnd> slots = new ArrayList<>();
        for(int i=0; i<3; i++){
            String date = DateHandler.getDate(i);
            List<StartEnd> slotsForDate = getRoomsTimeSlotsForDate(roomName, date);
            slots = Stream.concat(slots.stream(), slotsForDate.stream()).toList();
        }
        return slots;
    }

    public List<Reservation> getRoomReservations(String roomName) {
        Room room = getRoomByName(roomName);
        return room.getReservationList();
    }
    

    public boolean isSlotReserved(List<Reservation> reservations , Timestamp startDate, Timestamp endDate, Timestamp now){
        Stream<Reservation> stream = reservations.stream();
        boolean isReserved = stream.anyMatch(reservation-> reservation.getStartDate().equals(startDate) && reservation.getEndDate().equals(endDate));
        return isReserved;
    }

    public Room saveRoom(RoomForm roomForm){
        Room room = new Room(roomForm.getName(), roomForm.getStartTime(), roomForm.getEndTime(), roomForm.getReservationTimeInMinutes());
        Optional<Room> roomByName = roomRepository.findByName(room.getName());
        if( roomByName.isPresent()){
            throw new IllegalStateException("Room name taken");
        }
        if (roomForm.getStartTime().compareTo(roomForm.getEndTime()) >= 0) {
            throw new IllegalStateException("Start before stop");
        }
        log.info("Saving new room {} to the database", room.getName());
        roomRepository.save(room);
        return room;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class StartEnd{
        String start;
        String end;
        boolean isReserved;
        String date;
    }
}
