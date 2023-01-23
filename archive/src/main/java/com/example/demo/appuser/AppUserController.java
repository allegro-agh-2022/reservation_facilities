package com.example.demo.appuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppUserController {

    private final AppUserServiceImpl appUserServiceImpl;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(appUserServiceImpl.getUsers());
    }


    @Data
    class RoleToUserForm {
        private String email;
        private String roleName;
    }

    @Data
    @AllArgsConstructor
    class UserWithoutPassword {
        private Long id;
        private String email;
        private String name;
        private String surname;
    }
}

