package com.example.demo.malfunction;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserServiceImpl;
import com.example.demo.room.Room;
import com.example.demo.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MalfunctionService {
    private final MalfunctionRepository malfunctionRepository;
    private final AppUserServiceImpl appUserService;
    private final RoomService roomService;
    public List<Malfunction> getAllMalfunctions() {
        return malfunctionRepository.findAll();
    }

    public Malfunction createMalfunction(MalfunctionForm malfunctionForm, String email) {
        AppUser appUser = appUserService.getAppUser(email);
        Room room = roomService.getRoomByName(malfunctionForm.roomName());
        Malfunction malfunction = Malfunction.builder().description(malfunctionForm.description())
                .title(malfunctionForm.title())
                .appUser(appUser)
                .room(room)
                .isResolved(false).build();
        malfunctionRepository.save(malfunction);
        appUser.getMalfunctions().add(malfunction);
        room.getMalfunctions().add(malfunction);
        return malfunction;

    }


    public void resolveMalfunction(Long id, boolean isResolved) {
        Malfunction malfunction = malfunctionRepository.getById(id);
        malfunction.setResolved(isResolved);
        malfunctionRepository.save(malfunction);
    }
}
