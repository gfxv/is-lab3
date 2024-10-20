package dev.gfxv.lab1.controller;

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
@RequestMapping("/api/coordinates")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CoordinatesController {

    CoordinateRepository coordinateRepository;

    @Autowired
    public CoordinatesController(
        CoordinateRepository coordinateRepository
    ) {
        this.coordinateRepository = coordinateRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllCoordinates() {
        return ResponseEntity.ok(coordinateRepository.findAll());
    }
}
