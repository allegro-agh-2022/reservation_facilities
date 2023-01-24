package com.example.demo.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationForm {
    private String startDate;
    private String endDate;
    private String roomName;
}
