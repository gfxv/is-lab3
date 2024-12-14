package dev.gfxv.lab2.controller;

import dev.gfxv.lab2.utils.JwtParser;
import dev.gfxv.lab2.dto.CoordinatesDTO;
import dev.gfxv.lab2.exceptions.UserNotFoundException;
import dev.gfxv.lab2.security.JwtProvider;
import dev.gfxv.lab2.service.CoordinateService;
import dev.gfxv.lab2.service.SpaceMarineService;
import dev.gfxv.lab2.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/coordinates")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CoordinatesController {

    // TODO: move to service
    CoordinateService coordinateService;

    JwtProvider jwtProvider;
    SpaceMarineService spaceMarineService;
    UserService userService;

    @Autowired
    public CoordinatesController(
        CoordinateService coordinateService,
        JwtProvider jwtProvider,
        SpaceMarineService spaceMarineService,
        UserService userService

    ) {
        this.coordinateService = coordinateService;
        this.jwtProvider = jwtProvider;
        this.spaceMarineService = spaceMarineService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCoordinates() {
        return ResponseEntity.ok(coordinateService.getAllCoordinates());
    }

    @GetMapping("/my")
    public ResponseEntity<?> getChaptersByUserId(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);
        List<CoordinatesDTO> chapters = coordinateService.getChaptersByUser(username);
        return new ResponseEntity<>(chapters, HttpStatus.OK);
    }

    @DeleteMapping("/{coordinateId}")
    public ResponseEntity<?> deleteMarine(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String coordinateId
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);

        try {
            Long cdId = Long.parseLong(coordinateId);
            Long userId = userService.getIdByUsername(username);
            spaceMarineService.deleteMarineByCoordinateForUser(cdId, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
