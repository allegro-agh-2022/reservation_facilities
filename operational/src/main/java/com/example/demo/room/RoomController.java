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

    @PostMapping("/room/save")
    public ResponseEntity<Room> saveRoom(@RequestBody RoomForm roomForm){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/room/save").toUriString());
        return ResponseEntity.created(uri).body(roomService.saveRoom(roomForm));
    }

    @PutMapping("/room/edit")
    public ResponseEntity<Room> editRoom(@RequestBody RoomForm roomForm){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/room/edit").toUriString());
        return ResponseEntity.created(uri).body(roomService.editRoom(roomForm));
    }

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

    @DeleteMapping("/room/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {

        boolean isRemoved = roomService.deleteRoom(id);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }
}

