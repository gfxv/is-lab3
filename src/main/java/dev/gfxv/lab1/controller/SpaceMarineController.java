package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.JwtParser;
import dev.gfxv.lab1.dao.enums.MeleeWeapon;
import dev.gfxv.lab1.dao.enums.Weapon;
import dev.gfxv.lab1.dto.SpaceMarineDTO;
import dev.gfxv.lab1.dto.ws.ResponseType;
import dev.gfxv.lab1.dto.ws.TableRecordsResponse;
import dev.gfxv.lab1.exceptions.NotFoundException;
import dev.gfxv.lab1.exceptions.UserNotFoundException;
import dev.gfxv.lab1.security.JwtProvider;
import dev.gfxv.lab1.service.SpaceMarineService;
import dev.gfxv.lab1.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/marines")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceMarineController {

    int maxPageSize = 10;

    SpaceMarineService spaceMarineService;
    UserService userService;

    SimpMessagingTemplate messagingTemplate;
    JwtProvider jwtProvider;

    @Autowired
    public SpaceMarineController(
        SpaceMarineService spaceMarineService,
        UserService userService,
        SimpMessagingTemplate messagingTemplate,
        JwtProvider jwtProvider
    ) {
        this.spaceMarineService = spaceMarineService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMarineById(
        @PathVariable Long id
    ) {
        try {
            SpaceMarineDTO marine = spaceMarineService.getMarineById(id);
            return new ResponseEntity<>(marine, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
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

    @GetMapping("")
    public ResponseEntity<?> getMarinesByPage(
        @RequestParam int page,
        @RequestParam int size
    ) {
        if (page > maxPageSize) {
            return new ResponseEntity<>(String.format("Maximum page size is %d", maxPageSize), HttpStatus.BAD_REQUEST);
        }

        List<SpaceMarineDTO> marines = spaceMarineService.getAllMarinesAsPage(page, size);
        TableRecordsResponse response = TableRecordsResponse.builder()
                .type(ResponseType.GET)
                .records(marines)
                .build();
        messagingTemplate.convertAndSend("/records/changes", response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updateMarine(
        @RequestBody SpaceMarineDTO spaceMarineDTO
    ) {
        try {
            spaceMarineService.updateMarine(spaceMarineDTO);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{marineId}")
    public ResponseEntity<?> deleteMarine(
        @RequestHeader("Authorization") String tokenHeader,
        @PathVariable String marineId
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);

        try {
            Long id = Long.parseLong(marineId);
            if (!userService.validateDeletePermission(username, id)) {
                return new ResponseEntity<>("Permission Denied", HttpStatus.FORBIDDEN);
            }
            spaceMarineService.deleteMarineById(Long.parseLong(marineId));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/has-access/{marineId}")
    public ResponseEntity<?> hasAccess(
        @RequestHeader("Authorization") String tokenHeader,
        @PathVariable String marineId
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);

        try {
            Long id = Long.parseLong(marineId);
            return new ResponseEntity<>(userService.validateDeletePermission(username, id), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/count")
    public ResponseEntity<?> countMarines() {
        return new ResponseEntity<>(spaceMarineService.countMarines(), HttpStatus.OK);
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
