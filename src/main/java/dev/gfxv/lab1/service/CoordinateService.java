package dev.gfxv.lab1.service;

import dev.gfxv.lab1.dto.ChapterDTO;
import dev.gfxv.lab1.dto.CoordinatesDTO;
import dev.gfxv.lab1.repository.CoordinateRepository;
import dev.gfxv.lab1.repository.SpaceMarineRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CoordinateService {

    CoordinateRepository coordinateRepository;
    SpaceMarineRepository spaceMarineRepository;

    @Autowired
    public CoordinateService(
        CoordinateRepository coordinateRepository,
        SpaceMarineRepository spaceMarineRepository
    ) {
        this.coordinateRepository = coordinateRepository;
        this.spaceMarineRepository = spaceMarineRepository;
    }

    public List<CoordinatesDTO> getAllCoordinates() {
        return coordinateRepository.findAll()
                .stream()
                .map(CoordinatesDTO::fromDAO)
                .toList();
    }

    public List<CoordinatesDTO> getChaptersByUser(String username) {
        return spaceMarineRepository.findAllByUserUsername(username)
                .stream()
                .map(dao -> CoordinatesDTO.fromDAO(dao.getCoordinates()))
                .distinct()
                .toList();
    }
}
