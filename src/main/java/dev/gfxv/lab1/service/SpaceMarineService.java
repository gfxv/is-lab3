package dev.gfxv.lab1.service;

import dev.gfxv.lab1.dao.ChapterDAO;
import dev.gfxv.lab1.dao.CoordinatesDAO;
import dev.gfxv.lab1.dao.SpaceMarineDAO;
import dev.gfxv.lab1.dao.UserDAO;
import dev.gfxv.lab1.dto.ChapterDTO;
import dev.gfxv.lab1.dto.CoordinatesDTO;
import dev.gfxv.lab1.dto.SpaceMarineDTO;
import dev.gfxv.lab1.exceptions.NotFoundException;
import dev.gfxv.lab1.exceptions.UserNotFoundException;
import dev.gfxv.lab1.repository.ChapterRepository;
import dev.gfxv.lab1.repository.CoordinateRepository;
import dev.gfxv.lab1.repository.SpaceMarineRepository;
import dev.gfxv.lab1.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceMarineService {

    SpaceMarineRepository spaceMarineRepository;
    CoordinateRepository coordinateRepository;
    ChapterRepository chapterRepository;
    UserRepository userRepository;

    @Autowired
    public SpaceMarineService(
        SpaceMarineRepository spaceMarineRepository,
        CoordinateRepository coordinateRepository,
        ChapterRepository chapterRepository,
        UserRepository userRepository
    ) {
        this.spaceMarineRepository = spaceMarineRepository;
        this.coordinateRepository = coordinateRepository;
        this.chapterRepository = chapterRepository;
        this.userRepository = userRepository;
    }

    public SpaceMarineDTO getMarineById(Long id) throws NotFoundException {
        Optional<SpaceMarineDAO> marine = spaceMarineRepository.findById(id);
        if (marine.isEmpty()) {
            throw new NotFoundException("Space Marine not found");
        }
        return SpaceMarineDTO.fromDAO(marine.get());
    }

    public void updateMarine(SpaceMarineDTO marine) throws NotFoundException {
        // TODO: add record to 'changes' table

        Optional<SpaceMarineDAO> marineOptional = spaceMarineRepository.findById(marine.getId());
        if (marineOptional.isEmpty()) {
            throw new NotFoundException("Marine not found");
        }
        SpaceMarineDAO updatedMarine = SpaceMarineDTO.toDAO(marine);
        updatedMarine.setUser(marineOptional.get().getUser());
        spaceMarineRepository.save(updatedMarine);
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

        // ДАЛЬШЕ БОГА НЕТ

        // check if coordinate exists, create if not
        CoordinatesDAO coordinate;
        CoordinatesDTO coordinateDTO = spaceMarineDTO.getCoordinates();
        Optional<CoordinatesDAO> foundCoordinate = coordinateRepository.findByXAndY(coordinateDTO.getX(), coordinateDTO.getY());
        if (foundCoordinate.isEmpty()) {
            coordinate = coordinateRepository.save(CoordinatesDTO.toDAO(coordinateDTO));
            spaceMarine.setCoordinates(coordinate);
        } else {
            spaceMarine.setCoordinates(foundCoordinate.get());
        }

        // check if chapter exists, create if not
        ChapterDAO chapter;
        ChapterDTO chapterDTO = spaceMarineDTO.getChapter();
        Optional<ChapterDAO> foundChapter = chapterRepository
                .findByNameAndParentLegionAndMarinesCountAndWorld(
                        chapterDTO.getName(),
                        chapterDTO.getParentLegion(),
                        chapterDTO.getMarinesCount(),
                        chapterDTO.getWorld()
                );
        if (foundChapter.isEmpty()) {
            chapter = chapterRepository.save(ChapterDTO.toDAO(chapterDTO));
            spaceMarine.setChapter(chapter);
        } else {
            spaceMarine.setChapter(foundChapter.get());
        }

        spaceMarineRepository.save(spaceMarine);
    }

    public void deleteMarineById(Long id) throws NotFoundException {
        Optional<SpaceMarineDAO> marine = spaceMarineRepository.findById(id);
        if (marine.isEmpty()) {
            throw new NotFoundException("Space Marine not found");
        }
        spaceMarineRepository.deleteById(id);
    }

    public List<SpaceMarineDTO> getAllMarinesAsPage(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        return spaceMarineRepository.findAll(paging)
                .get()
                .map(SpaceMarineDTO::fromDAO)
                .toList();
    }

    public long countMarines() {
        return spaceMarineRepository.count();
    }
}
