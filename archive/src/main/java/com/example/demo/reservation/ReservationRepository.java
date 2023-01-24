package com.example.demo.reservation;


import com.example.demo.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    void deleteById(Long aLong);

    @Override
    Optional<Reservation> findById(Long aLong);

    List<Reservation> findReservationsByRoom(Room room);

}
