package com.example.demo.malfunction;

import com.example.demo.appuser.AppUser;
import com.example.demo.room.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonManagedReference
    @JsonIgnoreProperties({"reservationList", "roles"})
    private AppUser appUser;

    private boolean isResolved = false;

    public Malfunction() {

    }
}
