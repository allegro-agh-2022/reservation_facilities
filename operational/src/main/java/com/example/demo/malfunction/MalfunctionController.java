package com.example.demo.malfunction;

import com.example.demo.utils.AuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MalfunctionController {
    private final MalfunctionService malfunctionService;

    @GetMapping("/malfunctions")
    public ResponseEntity<List<Malfunction>> getAllMalfunctions(){
        return ResponseEntity.ok().body(malfunctionService.getAllMalfunctions());
    }
    @PostMapping("/malfunctions/create")
    public ResponseEntity<Malfunction> createMalfunction(HttpServletRequest request, @RequestBody MalfunctionForm malfunctionForm){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/malfunctions").toUriString());
        String email = AuthHandler.getCurrentUserEmail(request.getHeader(AUTHORIZATION));
        return ResponseEntity.created(uri).body(malfunctionService.createMalfunction(malfunctionForm, email));
    }

    @PutMapping(path = "/malfunctions/{malfunctionId}")
    public void resolveMalfunction(
            @PathVariable("malfunctionId") Long id,
            @RequestParam boolean isResolved) {
        malfunctionService.resolveMalfunction(id, isResolved);
    }
}
