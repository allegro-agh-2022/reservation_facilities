package com.example.demo.appuser;

import com.example.demo.reservation.Reservation;
import com.example.demo.reservation.ReservationForm;
import com.example.demo.room.Room;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AppUserServiceUnitTest {
    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void getUsers_should_return_users(){
        // Arrange
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        when(appUserRepository.findAll()).thenReturn(List.of(appUser));

        // Act
        final var result = appUserService.getUsers();

        // Assert
        assertThat(result).isEqualTo(List.of(appUser));
    }

    @Test
    void saveAppUser_should_save_appUser() {
        // Arrange
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        when(appUserRepository.findUserByEmail(any(String.class))).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("pass");

        // Act
        final var result = appUserService.saveAppUser(appUser);

        // Assert
        assertThat(result).isEqualTo(appUser);
    }

    @Test
    void saveRole_should_save_role(){
        // Arrange
        Role role = new Role(1L, "ROLE_USER");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        final var result = appUserService.saveRole(role);

        // Assert
        assertThat(result).isEqualTo(role);
    }

    @Test
    void addRoleToAppUser_should_assign_role_to_user(){
        // Arrange
        Role role = new Role(1L, "ROLE_USER");
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        when(roleRepository.findByName(any(String.class))).thenReturn(role);
        when(appUserRepository.findUserByEmail(any(String.class))).thenReturn(java.util.Optional.of(appUser));

        // Act
        appUserService.addRoleToAppUser("gabi@mail.com", "ROLE_USER");

        // Assert
        verify(roleRepository, times(1)).findByName(any(String.class));
        verify(appUserRepository, times(1)).findUserByEmail(any(String.class));
    }

    @Test
    void getAppUser_should_return_app_user(){
        // Arrange
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        when(appUserRepository.findUserByEmail(any(String.class))).thenReturn(java.util.Optional.of(appUser));

        // Act
        final var result = appUserService.getAppUser("gabi@mail.com");

        // Assert
        assertThat(result).isEqualTo(appUser);
        verify(appUserRepository, times(1)).findUserByEmail(any(String.class));
    }

    @Test
    void updateAppUser_should_throw_when_email_taken(){
        // Arrange
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        appUser.setId(1L);
        when(appUserRepository.findById(any(Long.class))).thenReturn(Optional.of(appUser));
        when(appUserRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(appUser));

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            appUserService.updateAppUser(appUser.getId(), appUser.getName(), appUser.getSurname(), "newEmail@email.com", appUser.getEmail());
        });

        String expectedMessage = "Email taken";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateUserPassword_should_change_password(){
        // Arrange
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        appUser.setId(1L);
        when(appUserRepository.findById(any(Long.class))).thenReturn(Optional.of(appUser));
        when(passwordEncoder.encode(any(String.class))).thenReturn("pass");
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        // Act
        appUserService.updateUserPassword(appUser.getId(), "pass","newPass", "gabi@mail.com" );

        // Assert
        verify(appUserRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void updateUserPassword_should_fail_with_wrong_email(){
        // Arrange
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        appUser.setId(1L);
        when(appUserRepository.findById(any(Long.class))).thenReturn(Optional.of(appUser));

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            appUserService.updateUserPassword(appUser.getId(), "pass","newPass", "differentperson@mail.com" );
        });

        String expectedMessage = "Cannot change someone else password";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void loadUserByUsername_should_load_user(){
        // Arrange
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        appUser.setId(1L);
        when(appUserRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(appUser));

        // Act
        final var result = appUserService.loadUserByUsername("gabi@mail.com");

        // Assert
        assertThat(result.getUsername()).isEqualTo(appUser.getEmail());
        verify(appUserRepository, times(1)).findUserByEmail(any(String.class));
    }

    @Test
    void getReservationsByEmail_should_return_reservatins(){
        // Arrange
        Room room = new Room("tenis stołowy", "06:00:00", "22:30:00", 60);
        AppUser appUser = new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass");
        Reservation reservation = new Reservation("21-01-2023 15:00:00.0","21-01-2023 15:30:00.0", room, appUser);
        when(appUserRepository.findReservationsByEmail(any(String.class))).thenReturn(List.of(reservation));

        // Act
        final var result = appUserService.getReservationsByEmail("gabi@mail.com");

        // Assert
        assertThat(result).isEqualTo(List.of(reservation));
    }

    @Test
    void getUserRole_should_return_user_roles(){
        // Arrange
        Role role = new Role(1L, "ROLE_USER");
        when(appUserRepository.findRolesByEmail(any(String.class))).thenReturn(List.of(role));

        // Act
        final var result = appUserService.getUserRole("gabi@mail.com");

        // Assert
        assertThat(result).isEqualTo(List.of(role));
    }
}
