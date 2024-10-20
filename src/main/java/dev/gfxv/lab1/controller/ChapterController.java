package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.repository.ChapterRepository;
import dev.gfxv.lab1.repository.CoordinateRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/chapters")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterController {

    ChapterRepository chapterRepository;

    @Autowired
    public ChapterController(
        ChapterRepository chapterRepository
    ) {
        this.chapterRepository = chapterRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllChapters() {
        return ResponseEntity.ok(chapterRepository.findAll());
    }
}
