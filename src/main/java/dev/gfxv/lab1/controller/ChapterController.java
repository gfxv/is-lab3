package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.JwtParser;
import dev.gfxv.lab1.dto.ChapterDTO;
import dev.gfxv.lab1.security.JwtProvider;
import dev.gfxv.lab1.service.ChapterService;
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

    @Autowired
    public ChapterController(
            ChapterService chapterService,
            JwtProvider jwtProvide
    ) {
        this.chapterService = chapterService;
        this.jwtProvider = jwtProvide;
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
}
