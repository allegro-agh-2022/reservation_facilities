package com.example.demo.reservation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService reservationService;


    @GetMapping("/reservations/{name}")
    public ResponseEntity<List<Reservation>> getRoomReservations(@PathVariable String name) {
        return ResponseEntity.ok().body(reservationService.getRoomReservations(name));
    }

    @GetMapping("reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok().body(reservationService.getReservations());
    }
}
