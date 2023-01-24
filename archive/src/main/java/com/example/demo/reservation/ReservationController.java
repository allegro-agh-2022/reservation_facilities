package com.example.demo.reservation;

import com.example.demo.appuser.AppUserServiceImpl;
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
    private final AppUserServiceImpl appUserService;


    @GetMapping("/reservations/searchByRoom")
    public ResponseEntity<List<Reservation>> getRoomReservations(@RequestParam String room) {
        return ResponseEntity.ok().body(reservationService.getRoomReservations(room));
    }

    @GetMapping("/reservations/searchByEmail")
    public ResponseEntity<List<Reservation>> getUserReservations(@RequestParam String email) {
        return ResponseEntity.ok().body(appUserService.getReservationsByEmail(email));
    }

    @GetMapping("reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok().body(reservationService.getReservations());
    }
}
