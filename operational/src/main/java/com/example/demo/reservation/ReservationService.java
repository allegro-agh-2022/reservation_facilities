package com.example.demo.reservation;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserServiceImpl;
import com.example.demo.appuser.Role;
import com.example.demo.room.Room;
import com.example.demo.room.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AppUserServiceImpl appUserService;
    private final RoomService roomService;
    public Reservation saveReservation(ReservationController.ReservationForm reservationForm, String email){
        Room room = roomService.getRoomByName(reservationForm.getRoomName());
        AppUser appUser = appUserService.getAppUser(email);
        Reservation reservation = new Reservation(reservationForm.getStartDate(), reservationForm.getEndDate(), room, appUser);
        List<Reservation> reservations = getRoomReservations(reservationForm.getRoomName());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        boolean isSlotReserved = roomService.isSlotReserved(reservations, reservation.getStartDate(), reservation.getEndDate(), now);
        if (isSlotReserved){
            throw new IllegalArgumentException("already reserved");
        }
        reservationRepository.save(reservation);
        room.addReservation(reservation);
        appUser.getReservationList().add(reservation);
        return reservation;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public boolean deleteReservation(Long id, String email){
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()){
            throw new IllegalArgumentException("no such reservation");
        }
        AppUser user = reservation.get().getAppUser();
        AppUser currentUser = appUserService.getAppUser(email);
        Role adminRole = currentUser.getRoles().stream().filter(role -> "ROLE_ADMIN".equals(role.getName())).findFirst().orElse(null);
        if(!Objects.equals(user.getEmail(), email) && adminRole==null){
            throw new IllegalStateException("Please delete your own reservations only");
        }
        reservationRepository.deleteById(id);
        return true;
    }

    public List<Reservation> getRoomReservations(String name) {
        //return reservationRepository.findByRoom(name);
        List<Reservation> reservations = new ArrayList<>();
        return reservations;
    }

    public List<Reservation> geUserReservations(String email) {
        List<Reservation> reservations = appUserService.getReservationsByEmail(email);
        return reservations;
    }
}
