package com.example.demo.room;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class RoomForm {
    private String name;
    private String startTime;
    private String endTime;
    private int reservationTimeInMinutes;
}
