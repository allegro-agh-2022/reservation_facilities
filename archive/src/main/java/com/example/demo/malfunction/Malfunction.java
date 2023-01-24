package com.example.demo.malfunction;

import com.example.demo.appuser.AppUser;
import com.example.demo.room.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Malfunction {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String description;
    private String title;

    @ManyToOne
    @JoinColumn(name="room_id")
    @JsonIgnore
    private Room room;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnoreProperties({"reservationList", "roles"})
    private AppUser appUser;

    private boolean isResolved = false;

    public Malfunction() {

    }
}
