package com.example.demo.appuser;


import com.example.demo.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(appUser.getEmail());
        if (Stream.of(appUser.getName(), appUser.getEmail(), appUser.getPassword(), appUser.getSurname())
                        .anyMatch(str-> str == null || str.isEmpty())) {
            throw new IllegalStateException("All fields must be filled");
        }
        if (!matcher.matches()) {
            throw new IllegalStateException("Email does not match pattern");
        }
        Optional<AppUser> userByEmail = appUserRepository.findUserByEmail(appUser.getEmail());
        if (userByEmail.isPresent()) {
            throw new IllegalStateException("Email taken");
        }
        log.info("Saving new user {} to the database", appUser.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
        return appUser;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database ", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToAppUser(String email, String roleName) {
        Optional<AppUser> appUser = appUserRepository.findUserByEmail(email);
        Role role = roleRepository.findByName(roleName);
        appUser.ifPresent(user -> user.getRoles().add(role));

    }

    @Override
    public AppUser getAppUser(String email) {
        Optional<AppUser> appUser = appUserRepository.findUserByEmail(email);
        if (appUser.isEmpty()) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        }
        return appUser.get();
    }


    public void deleteAppUser(Long id) {
        boolean exists = appUserRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException(
                    "user with id " + id + " does not exist"
            );
        }
        appUserRepository.deleteById(id);
    }


    @Transactional
    public void updateAppUser(Long id, String name, String surname, String email, String usersEmail) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + id + "does not exist"
                ));

        if (!Objects.equals(user.getEmail(), usersEmail)){
            throw new IllegalStateException("Cannot change someone else password");
        }

        if (name != null && name.length() > 0 && !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        if (surname != null && surname.length() > 0 && !Objects.equals(user.getSurname(), surname)) {
            user.setSurname(surname);
        }

        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
            Optional<AppUser> userByEmail = appUserRepository.findUserByEmail(email);
            if (userByEmail.isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            user.setEmail(email);
        }
    }

    public void updateUserPassword(Long id, String oldPassword, String newPassword, String email) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + id + "does not exist"
                ));

        if (!Objects.equals(user.getEmail(), email)){
            throw new IllegalStateException("Cannot change someone else password");
        }

        if (oldPassword != null && newPassword != null && newPassword.length() > 0 ) {
            if(!passwordEncoder.matches(oldPassword,user.getPassword())) {
                throw new IllegalStateException("To change password put correct old password");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findUserByEmail(email);
        if (appUser.isEmpty()) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        }
        AppUser user = appUser.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public List<Reservation> getReservationsByEmail(String email){
        List<Reservation> reservations = appUserRepository.findReservationsByEmail(email);
        return reservations;
    }

    @Override
    public List<Role> getUserRole(String email) {
        List<Role> roles = appUserRepository.findRolesByEmail(email);
        return roles;
    }
}
