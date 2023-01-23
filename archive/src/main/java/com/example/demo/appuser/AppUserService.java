package com.example.demo.appuser;

import com.example.demo.reservation.Reservation;

import java.util.List;

public interface AppUserService {

    AppUser saveAppUser(AppUser appUser);

    Role saveRole(Role role);

    void addRoleToAppUser(String email, String roleName);

    AppUser getAppUser(String email);
    List<AppUser> getUsers();
    List<Reservation> getReservationsByEmail(String email);
    public List<Role> getUserRole(String email);
}
