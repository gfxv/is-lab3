package dev.gfxv.lab1.service;

import dev.gfxv.lab1.dao.UserDAO;
import dev.gfxv.lab1.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userHasRole(String username, String role) {
        UserDAO user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username '%s' not found", username)));

        return user.getRoles()
                .stream()
                .anyMatch(userRole -> userRole.getName().equals(role));
    }

}
