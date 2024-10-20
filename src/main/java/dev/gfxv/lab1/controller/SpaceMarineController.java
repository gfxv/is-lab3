package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.JwtParser;
import dev.gfxv.lab1.dao.enums.MeleeWeapon;
import dev.gfxv.lab1.dao.enums.Weapon;
import dev.gfxv.lab1.dto.SpaceMarineDTO;
import dev.gfxv.lab1.dto.ws.ResponseType;
import dev.gfxv.lab1.dto.ws.TableRecordsResponse;
import dev.gfxv.lab1.exceptions.UserNotFoundException;
import dev.gfxv.lab1.security.JwtProvider;
import dev.gfxv.lab1.service.SpaceMarineService;
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

    SimpMessagingTemplate messagingTemplate;
    JwtProvider jwtProvider;

    @Autowired
    public SpaceMarineController(
        SpaceMarineService spaceMarineService,
        SimpMessagingTemplate messagingTemplate,
        JwtProvider jwtProvider
    ) {
        this.spaceMarineService = spaceMarineService;
        this.messagingTemplate = messagingTemplate;
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

    @GetMapping("")
    public ResponseEntity<?> getMarinesByPage(
        @RequestParam int page,
        @RequestParam int size
    ) {
        if (page > maxPageSize) {
            return new ResponseEntity<>(String.format("Maximum page size is %d", maxPageSize), HttpStatus.BAD_REQUEST);
        }

        List<SpaceMarineDTO> marines = spaceMarineService.getAllMarinesAsPage(page, size);
        System.out.println("Marines to be sent: " + marines);

        TableRecordsResponse response = TableRecordsResponse.builder()
                .type(ResponseType.GET)
                .records(marines)
                .build();
        messagingTemplate.convertAndSend("/records/changes", response);
        return new ResponseEntity<>(HttpStatus.OK);
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
