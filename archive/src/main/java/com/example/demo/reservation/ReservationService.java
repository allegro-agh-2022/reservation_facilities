package com.example.demo.reservation;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserServiceImpl;
import com.example.demo.room.Room;
import com.example.demo.room.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AppUserServiceImpl appUserService;

    private final RoomService roomService;

    public Reservation saveReservation(ReservationForm reservationForm, String email){
        Room room = roomService.getRoomByName(reservationForm.getRoomName());
        AppUser appUser = appUserService.getAppUser(email);
        Reservation reservation = new Reservation(reservationForm.getStartDate(), reservationForm.getEndDate(), room, appUser, email);
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

    public List<Reservation> getRoomReservations(String name) {
        Room room = roomService.getRoomByName(name);
        return reservationRepository.findReservationsByRoom(room);
    }
}
