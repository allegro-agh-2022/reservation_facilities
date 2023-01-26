package com.example.demo.reservation;

import com.example.demo.appuser.AppUser;
import com.example.demo.room.Room;
import com.example.demo.utils.DateHandler;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table
@Data
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Reservation {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp creationDate;

    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name="reservation_id")
    private Room room;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="user_id")
    private AppUser appUser;

    private String userEmail;

    public Reservation(Long id, Date startDate, Date endDate, Room room, AppUser appUser, String userEmail) {
        java.util.Date utilDate = new java.util.Date();
        this.id = id;
        this.startDate = new Timestamp(startDate.getTime());
        this.endDate = new Timestamp(endDate.getTime());
        this.creationDate = new Timestamp(utilDate.getTime());
        this.room = room;
        this.appUser = appUser;
        this.userEmail = userEmail;
    }

    public Reservation(String startDate, String endDate, Room room, AppUser appUser, String userEmail){
        java.util.Date utilDate = new java.util.Date();
        this.startDate = new Timestamp(DateHandler.handleDate(startDate).getTime());
        this.endDate = new Timestamp(DateHandler.handleDate(endDate).getTime());
        this.creationDate = new Timestamp(utilDate.getTime());
        this.room = room;
        this.appUser = appUser;
        this.userEmail = userEmail;
    }

    public Reservation(String startDate, String endDate, Room room, AppUser appUser){
        java.util.Date utilDate = new java.util.Date();
        this.startDate = new Timestamp(DateHandler.handleDate(startDate).getTime());
        this.endDate = new Timestamp(DateHandler.handleDate(endDate).getTime());
        this.creationDate = new Timestamp(utilDate.getTime());
        this.room = room;
        this.appUser = appUser;
    }
}
