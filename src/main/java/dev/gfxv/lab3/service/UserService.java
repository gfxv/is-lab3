package dev.gfxv.lab3.service;

import dev.gfxv.lab3.dao.SpaceMarineDAO;
import dev.gfxv.lab3.dao.UserDAO;
import dev.gfxv.lab3.exceptions.UserNotFoundException;
import dev.gfxv.lab3.repository.SpaceMarineRepository;
import dev.gfxv.lab3.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    String ADMIN_ROLE = "ADMIN";

    UserRepository userRepository;
    SpaceMarineRepository spaceMarineRepository;

    @Autowired
    public UserService(UserRepository userRepository, SpaceMarineRepository spaceMarineRepository) {
        this.userRepository = userRepository;
        this.spaceMarineRepository = spaceMarineRepository;
    }

    public boolean userHasRole(String username, String role) {
        UserDAO user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username '%s' not found", username)));

        return user.getRoles()
                .stream()
                .anyMatch(userRole -> userRole.getName().equals(role));
    }

    public boolean isAdmin(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username '%s' not found", username)))
                .getRoles().stream()
                .anyMatch(userRole -> userRole.getName().equals(ADMIN_ROLE));
    }

    public Long getIdByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("username '%s' not found", username)))
                .getId();
    }

    public boolean userHasAccess(String username, Long marineId) {
        Optional<SpaceMarineDAO> spaceMarine = spaceMarineRepository.findById(marineId);
        if (spaceMarine.isEmpty()) {
            return false;
        }
        Optional<UserDAO> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return false;
        }
        return spaceMarine.get().getUser().equals(user.get());
    }

    public boolean validateDeletePermission(String username, Long marineId) {
        return userHasAccess(username, marineId) || isAdmin(username);
    }

}
