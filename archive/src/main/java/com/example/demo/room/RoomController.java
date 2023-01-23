package com.example.demo.room;

import com.example.demo.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;


    @GetMapping("/rooms/get")
    public ResponseEntity<List<Room>> getRooms(){
        return ResponseEntity.ok().body(roomService.getRooms());
    }

    @GetMapping("/room/{name}")
    public ResponseEntity<List<RoomService.StartEnd>> getSlots(@PathVariable String name) {
        return ResponseEntity.ok().body(roomService.getRoomsTimeSlotsForThreeDays(name));
    }

    @GetMapping("/room_reservations/{name}")
    public ResponseEntity<List<Reservation>> getRoomReservations(@PathVariable String name) {
        return ResponseEntity.ok().body(roomService.getRoomReservations(name));
    }
}

