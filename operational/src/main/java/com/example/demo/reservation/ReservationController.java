package com.example.demo.reservation;

import com.example.demo.appuser.AppUserServiceImpl;
import com.example.demo.utils.AuthHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService reservationService;
    private final RestTemplate restTemplate;

    @GetMapping("/archive/reservations")
    public ResponseEntity<List<Reservation>> getArchivedReservations() {
        ResponseEntity<List<Reservation>> response = restTemplate.exchange("http://archive-app:8081/api/v1/reservations", HttpMethod.GET, null, new ParameterizedTypeReference<List<Reservation>>() {
        });
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @GetMapping("/archive/reservations/searchByRoom")
    public ResponseEntity<List<Reservation>> getArchivedReservationsByRoom(@RequestParam String roomName) {
        Map<String, String> uriVariables = new HashMap<>();

        uriVariables.put("room", roomName);
        ResponseEntity<List<Reservation>> response = restTemplate.exchange("http://archive-app:8081/api/v1/reservations/searchByRoom?room={room}", HttpMethod.GET, null, new ParameterizedTypeReference<List<Reservation>>() {
        }, uriVariables);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @GetMapping("/archive/reservations/searchByUser")
    public ResponseEntity<List<Reservation>> getArchivedReservationsByUser(@RequestParam String email) {
        Map<String, String> uriVariables = new HashMap<>();

        uriVariables.put("email", email);
        ResponseEntity<List<Reservation>> response = restTemplate.exchange("http://archive-app:8081/api/v1/reservations/searchByEmail?email={email}", HttpMethod.GET, null, new ParameterizedTypeReference<List<Reservation>>() {
        }, uriVariables);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @PostMapping("reservation/save")
    public ResponseEntity<String> saveReservation(HttpServletRequest request, @RequestBody ReservationForm reservationForm) {
        String email = AuthHandler.getCurrentUserEmail(request.getHeader(AUTHORIZATION));
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/reservation/save").toUriString());
        reservationService.saveReservation(reservationForm, email);
        return new ResponseEntity<>("created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/reservations/{name}")
    public ResponseEntity<List<Reservation>> getRoomReservations(@PathVariable String name) {
        return ResponseEntity.ok().body(reservationService.getRoomReservations(name));
    }

    @GetMapping("/reservations/my_reservations")
    public ResponseEntity<List<Reservation>> getUserReservations(HttpServletRequest request) {
        String email = AuthHandler.getCurrentUserEmail(request.getHeader(AUTHORIZATION));
        return ResponseEntity.ok().body(reservationService.geUserReservations(email));
    }

    @GetMapping("reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok().body(reservationService.getReservations());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteReservation(HttpServletRequest request, @PathVariable Long id) {

        String email = AuthHandler.getCurrentUserEmail(request.getHeader(AUTHORIZATION));
        boolean isRemoved = reservationService.deleteReservation(id, email);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }
}



