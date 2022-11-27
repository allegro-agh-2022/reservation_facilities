package com.example.demo.reservation;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    void deleteById(Long aLong);

    @Override
    Optional<Reservation> findById(Long aLong);

}
