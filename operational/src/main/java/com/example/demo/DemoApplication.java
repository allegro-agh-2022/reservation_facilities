package com.example.demo;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserService;
import com.example.demo.appuser.Role;
import com.example.demo.room.RoomForm;
import com.example.demo.room.RoomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        configuration.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    CommandLineRunner run(AppUserService appUserService, RoomService roomService) {
        return args -> {
            appUserService.saveRole(new Role(null, "ROLE_USER"));
            appUserService.saveRole(new Role(null, "ROLE_ADMIN"));
            appUserService.saveAppUser(new AppUser("Gabi", "Lesn", "gabi@mail.com", "pass"));
            appUserService.saveAppUser(new AppUser("Gabi", "Lesn4", "gabi4@mail.com", "pass"));
            appUserService.addRoleToAppUser("gabi@mail.com", "ROLE_USER");
            appUserService.addRoleToAppUser("gabi4@mail.com", "ROLE_ADMIN");
            roomService.saveRoom(new RoomForm("salka wspinaczkowa", "06:00:00", "22:30:00", 30));
            roomService.saveRoom(new RoomForm("tenis stołowy", "06:00:00", "22:30:00", 60));
            roomService.saveRoom(new RoomForm("bilard", "08:00:00", "23:00:00", 60));
        };

    }
}
