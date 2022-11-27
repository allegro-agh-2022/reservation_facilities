package com.example.demo.reservation;

import com.example.demo.appuser.AppUserServiceImpl;
import com.example.demo.utils.AuthHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService reservationService;
    private final AppUserServiceImpl appUserServiceImpl;

    @PostMapping("reservation/save")
    public ResponseEntity<String> saveReservation(HttpServletRequest request, @RequestBody ReservationForm reservationForm){
        String email = AuthHandler.getCurrentUserEmail(request.getHeader(AUTHORIZATION));
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/reservation/save").toUriString());
        reservationService.saveReservation(reservationForm, email);
        return ResponseEntity.created(uri).body("created successfully");
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
    public ResponseEntity<List<Reservation>> getReservations(){
        return ResponseEntity.ok().body(reservationService.getReservations());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {

        boolean isRemoved = reservationService.deleteReservation(id);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }
}

@Data
class ReservationForm{
    private String startDate;
    private String endDate;
    private String roomName;
}
