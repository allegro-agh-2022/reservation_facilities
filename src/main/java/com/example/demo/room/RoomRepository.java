package com.example.demo.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface RoomRepository extends JpaRepository<Room,Long> {
    @Query("select r from Room r where r.name = ?1")
    Optional<Room> findByName(String name);

//    @Query("select reservationList from Room where r.name = ?1")
//    List<Reservation> findReservationsByRoomName(String name);
}
