package com.example.demo.room;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RoomServiceUnitTest {
    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Test
    void saveRoom_should_save_room() {
        // Arrange
        RoomForm roomForm = new RoomForm("tenis stołowy", "06:00:00", "22:30:00", 60);
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        final var result = roomService.saveRoom(roomForm);

        // Assert
        assertThat(result).isEqualTo(room);
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void getRooms_should_return_rooms() {
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        when(roomRepository.findAll()).thenReturn(List.of(room));

        // Act
        final var result = roomService.getRooms();

        // Assert
        assertThat(result).isEqualTo(List.of(room));
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void getRoomByName_should_return_room() {
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        when(roomRepository.findByName(any(String.class))).thenReturn(java.util.Optional.of(room));

        // Act
        final var result = roomService.getRoomByName("tenis stołowy");

        // Assert
        assertThat(result).isEqualTo(room);
        verify(roomRepository, times(1)).findByName(any(String.class));
    }

    @Test
    void editRoom_should_edit_room() {
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        RoomForm roomForm = new RoomForm("tenis stołowy", "06:00:00", "22:30:00", 60);
        when(roomRepository.findByName(any(String.class))).thenReturn(java.util.Optional.of(room));

        // Act
        final var result = roomService.editRoom(roomForm);

        // Assert
        assertThat(result).isEqualTo(room);
        verify(roomRepository, times(1)).findByName(any(String.class));
    }

    @Test
    void deleteRoom_should_delete_room() {
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        room.setId(1L);
        when(roomRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(room));

        // Act
        final var result = roomService.deleteRoom(room.getId());

        // Assert
        assertThat(result).isEqualTo(true);
        verify(roomRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void getRoomsTimeSlots_should_return_timeslots(){
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        when(roomRepository.findByName(any(String.class))).thenReturn(java.util.Optional.of(room));
        var expected = createTimeSlots(room);

        // Act
        final var result = roomService.getRoomsTimeSlots("tenis stołowy");

        // Assert
        assertThat(result).size().isEqualTo(16);
        verify(roomRepository, times(1)).findByName(any(String.class));
    }

    @Test
    void getRoomsTimeSlotsForDate_should_return_correct_timeslots(){
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        when(roomRepository.findByName(any(String.class))).thenReturn(java.util.Optional.of(room));

        // Act
        final var result = roomService.getRoomsTimeSlotsForDate("tenis stołowy", "2021-05-01");

        // Assert
        assertThat(result).size().isEqualTo(16);
        //assertThat(result).isEqualTo(List.of("06:00:00", "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00", "22:00:00"));
        verify(roomRepository, times(2)).findByName(any(String.class));
    }

    @Test
    void getRoomsTimeSlotsForThreeDays_should_return_timeslots_for_three_days(){
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        when(roomRepository.findByName(any(String.class))).thenReturn(java.util.Optional.of(room));

        // Act
        final var result = roomService.getRoomsTimeSlotsForThreeDays("tenis stołowy");

        // Assert
        // assertThat(result).isEqualTo(List.of("06:00:00", "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00", "22:00:00", "06:00:00", "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00", "22:00:00", "06:00:00", "07:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00", "22:00:00"));
        assertThat(result).size().isEqualTo(48);
        verify(roomRepository, times(6)).findByName(any(String.class));
    }

    private ArrayList<RoomService.StartEnd> createTimeSlots(Room room) {
        LocalTime start = room.getStartTime().toLocalTime();
        LocalTime end = room.getEndTime().toLocalTime();
        int minutes = room.getReservationTimeInMinutes();
        ArrayList<RoomService.StartEnd> slots = new ArrayList<RoomService.StartEnd>();
        while(start.compareTo(end.plusMinutes(-minutes)) <= 0 ){
            RoomService.StartEnd startEnd = new RoomService.StartEnd();
            startEnd.setStart(start.toString());
            start = start.plusMinutes(minutes);
            startEnd.setEnd(start.toString());
            slots.add(startEnd);
        }
        return slots;
    }
}
