package dev.gfxv.lab1.service;

import dev.gfxv.lab1.dao.SpaceMarineDAO;
import dev.gfxv.lab1.dao.UserDAO;
import dev.gfxv.lab1.dto.SpaceMarineDTO;
import dev.gfxv.lab1.exceptions.UserNotFoundException;
import dev.gfxv.lab1.repository.SpaceMarineRepository;
import dev.gfxv.lab1.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceMarineService {

    final SpaceMarineRepository spaceMarineRepository;
    final UserRepository userRepository;

    @Autowired
    public SpaceMarineService(
        SpaceMarineRepository spaceMarineRepository,
        UserRepository userRepository
    ) {
        this.spaceMarineRepository = spaceMarineRepository;
        this.userRepository = userRepository;
    }

    public void newMarine(SpaceMarineDTO spaceMarineDTO, String owner) throws UserNotFoundException {
        final String op = "SpaceMarineService";
        System.out.printf("[%s] Received: %s\n", op, spaceMarineDTO);

        SpaceMarineDAO spaceMarine = SpaceMarineDTO.toDAO(spaceMarineDTO);
        spaceMarine.setCreationDate(LocalDate.now());

        Optional<UserDAO> user = userRepository.findByUsername(owner);
        if (user.isEmpty()) {
            throw new UserNotFoundException("username not found");
        }
        spaceMarine.setUser(user.get());
        spaceMarineRepository.save(spaceMarine);
    }

}
