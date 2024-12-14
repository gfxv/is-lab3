package dev.gfxv.lab2.controller;

import dev.gfxv.lab2.utils.JwtParser;
import dev.gfxv.lab2.dto.ChapterDTO;
import dev.gfxv.lab2.exceptions.UserNotFoundException;
import dev.gfxv.lab2.security.JwtProvider;
import dev.gfxv.lab2.service.ChapterService;
import dev.gfxv.lab2.service.SpaceMarineService;
import dev.gfxv.lab2.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: add token checks

@CrossOrigin
@RestController
@RequestMapping("/api/chapters")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterController {


    ChapterService chapterService;
    JwtProvider jwtProvider;
    SpaceMarineService spaceMarineService;
    UserService userService;

    @Autowired
    public ChapterController(
        ChapterService chapterService,
        JwtProvider jwtProvide,
        SpaceMarineService spaceMarineService,
        UserService userService
    ) {
        this.chapterService = chapterService;
        this.jwtProvider = jwtProvide;
        this.spaceMarineService = spaceMarineService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllChapters() {
        return ResponseEntity.ok(chapterService.getAllChapters());
    }

    @GetMapping("/my")
    public ResponseEntity<?> getChaptersByUserId(
        @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);
        List<ChapterDTO> chapters = chapterService.getChaptersByUser(username);
        return new ResponseEntity<>(chapters, HttpStatus.OK);
    }

    @DeleteMapping("/{chapterId}")
    public ResponseEntity<?> deleteMarine(
        @RequestHeader("Authorization") String tokenHeader,
        @PathVariable String chapterId
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);

        try {
            Long chId = Long.parseLong(chapterId);
            Long userId = userService.getIdByUsername(username);
            spaceMarineService.deleteMarineByChapterForUser(chId, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
