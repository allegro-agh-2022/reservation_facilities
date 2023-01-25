package demo.reservation;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserServiceImpl;
import com.example.demo.reservation.Reservation;
import com.example.demo.reservation.ReservationForm;
import com.example.demo.reservation.ReservationRepository;
import com.example.demo.reservation.ReservationService;
import com.example.demo.room.Room;
import com.example.demo.room.RoomService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ReservationServiceUnitTest {
    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private RoomService roomService;

    @Mock
    private AppUserServiceImpl appUserService;

    @Mock
    private ReservationRepository reservationRepository;


    @Test
    void saveReservation_should_save_reservation() {
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        room.setId(1L);
        ReservationForm reservationForm = new ReservationForm( "21-01-2023 15:00:00.0","21-01-2023 15:30:00.0","tenis stołowy");
        Reservation reservation = new Reservation("21-01-2023 15:00:00.0","21-01-2023 15:30:00.0", room, appUser);
        when(roomService.getRoomByName(any(String.class))).thenReturn(room);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(appUserService.getAppUser(any(String.class))).thenReturn(appUser);

        // Act
        final var result = reservationService.saveReservation(reservationForm, "email");

        // Assert
        assertThat(result.getStartDate()).isEqualTo(reservation.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(reservation.getEndDate());
        assertThat(result.getRoom()).isEqualTo(reservation.getRoom());
        assertThat(result.getAppUser()).isEqualTo(reservation.getAppUser());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void getReservations_should_return_reservations(){
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        Reservation reservation = new Reservation("21-01-2023 15:00:00.0","21-01-2023 15:30:00.0", room, appUser);
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // Act
        final var result = reservationService.getReservations();

        // Assert
        assertThat(result).size().isEqualTo(1);
        assertThat(result).isEqualTo(List.of(reservation));
    }

    @Test
    void getRoomReservations_should_return_room_reservation(){
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        Reservation reservation = new Reservation("21-01-2023 15:00:00.0","21-01-2023 15:30:00.0", room, appUser);
        reservation.setId(1L);
        when(roomService.getRoomByName(any(String.class))).thenReturn(room);
        when(reservationRepository.findReservationsByRoom(any(Room.class))).thenReturn(List.of(reservation));

        // Act
        final var result = reservationService.getRoomReservations("tenis stołowy");

        // Assert
        assertThat(result).size().isEqualTo(1);
        assertThat(result).isEqualTo(List.of(reservation));
    }
}
