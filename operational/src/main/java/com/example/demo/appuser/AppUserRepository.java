package com.example.demo.appuser;

import com.example.demo.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE u.email=?1")
    Optional<AppUser> findUserByEmail(String email);

    @Query("SELECT u.reservationList FROM AppUser u WHERE u.email=?1")
    List<Reservation> findReservationsByEmail(String email);

    @Query("SELECT u.roles FROM AppUser u WHERE u.email=?1")
    List<Role> findRolesByEmail(String email);
}
