package com.example.demo.appuser;

import com.example.demo.malfunction.Malfunction;
import com.example.demo.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.FetchType.EAGER;


@Entity
@Table
@Data
@AllArgsConstructor
@ToString(exclude = {"reservationList"})
public class AppUser {
    @Id
    @SequenceGenerator(name="user_sequence",
    sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;
    private String name;
    private String surname;
    private String email;

//    @JsonIgnore
    private String password;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy="appUser", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    private List<Malfunction> malfunctions = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Reservation> reservationList = new ArrayList<>();

    public AppUser(){

    }

    public AppUser(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
}
