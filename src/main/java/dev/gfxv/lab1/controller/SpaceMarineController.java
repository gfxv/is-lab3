package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.JwtParser;
import dev.gfxv.lab1.dao.enums.MeleeWeapon;
import dev.gfxv.lab1.dao.enums.Weapon;
import dev.gfxv.lab1.dto.SpaceMarineDTO;
import dev.gfxv.lab1.exceptions.UserNotFoundException;
import dev.gfxv.lab1.security.JwtProvider;
import dev.gfxv.lab1.service.SpaceMarineService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/marines")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceMarineController {

    SpaceMarineService spaceMarineService;
    JwtProvider jwtProvider;

    @Autowired
    public SpaceMarineController(
        SpaceMarineService spaceMarineService,
        JwtProvider jwtProvider
    ) {
        this.spaceMarineService = spaceMarineService;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/new")
    public ResponseEntity<?> newMarine(
        @RequestHeader("Authorization") String tokenHeader,
        @RequestBody SpaceMarineDTO spaceMarineDTO
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);

        try {
            spaceMarineService.newMarine(spaceMarineDTO, username);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/weapons")
    public List<String> getWeapons() {
        return Arrays.stream(Weapon.values())
                .map(Weapon::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/melee-weapons")
    public List<String> getMeleeWeapons() {
        return Arrays.stream(MeleeWeapon.values())
                .map(MeleeWeapon::getName)
                .collect(Collectors.toList());
    }
}
