package dev.gfxv.lab2.service;

import dev.gfxv.lab2.dao.*;
import dev.gfxv.lab2.dao.enums.Weapon;
import dev.gfxv.lab2.dto.ChapterDTO;
import dev.gfxv.lab2.dto.CoordinatesDTO;
import dev.gfxv.lab2.dto.EditHistoryDTO;
import dev.gfxv.lab2.dto.SpaceMarineDTO;
import dev.gfxv.lab2.exceptions.NotFoundException;
import dev.gfxv.lab2.exceptions.UserNotFoundException;
import dev.gfxv.lab2.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceMarineService {

    SpaceMarineRepository spaceMarineRepository;
    EditHistoryRepository editHistoryRepository;
    CoordinateRepository coordinateRepository;
    ChapterRepository chapterRepository;
    UserRepository userRepository;

    String noChapterName = "No Chapter";

    @Autowired
    public SpaceMarineService(
        SpaceMarineRepository spaceMarineRepository,
        EditHistoryRepository editHistoryRepository,
        CoordinateRepository coordinateRepository,
        ChapterRepository chapterRepository,
        UserRepository userRepository
    ) {
        this.spaceMarineRepository = spaceMarineRepository;
        this.editHistoryRepository = editHistoryRepository;
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

    public void updateMarine(SpaceMarineDTO marine, String editorUsername) throws NotFoundException {
        Optional<SpaceMarineDAO> marineOptional = spaceMarineRepository.findById(marine.getId());
        if (marineOptional.isEmpty()) {
            throw new NotFoundException("Marine not found");
        }

        if (marine.getChapter() == null) {
            ChapterDAO noChapter = chapterRepository.findByName(noChapterName)
                    .orElseThrow(() -> new NotFoundException("No Empty Chapter"));
            marine.setChapter(ChapterDTO.fromDAO(noChapter));
        }

        SpaceMarineDAO updatedMarine = SpaceMarineDTO.toDAO(marine);
        updatedMarine.setUser(marineOptional.get().getUser());

        spaceMarineRepository.save(updatedMarine);

        UserDAO editor = userRepository.findByUsername(editorUsername)
                .orElseThrow(() -> new NotFoundException("User not found"));
        newHistoryRecord(updatedMarine, editor);
    }

    public void newMarine(SpaceMarineDTO spaceMarineDTO, String owner) throws UserNotFoundException {
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

    public Map<String, List<SpaceMarineDTO>> getAggregated() {
        Map<String, List<SpaceMarineDTO>> aggregated = new HashMap<>();
        Weapon[] weapons = Weapon.values();
        for (Weapon weapon : weapons) {
            List<SpaceMarineDTO> marines = spaceMarineRepository.findAllByWeapon(weapon)
                    .stream()
                    .map(SpaceMarineDTO::fromDAO)
                    .toList();
            aggregated.put(weapon.getName(), marines);
        }
        return aggregated;
    }

    public void newHistoryRecord(SpaceMarineDAO marine, UserDAO editor) {
        EditHistoryDAO record = new EditHistoryDAO();
        record.setMarine(marine);
        record.setEditDate(LocalDate.now());
        record.setUser(editor);
        editHistoryRepository.save(record);
    }

    public List<EditHistoryDTO> getEditHistoryByMarineId(Long marineId) {
        return editHistoryRepository.findAllByMarineId(marineId)
                .stream()
                .map(EditHistoryDTO::fromDAO)
                .toList();

    }

    public int countMarinesWithHeight(Long height) {
        return spaceMarineRepository.countAllByHeight(height);
    }

    @Transactional
    public void deleteMarineByChapterForUser(Long chapterId, Long userId) {
        spaceMarineRepository.deleteAllByChapterIdAndUserId(chapterId, userId);
    }

    @Transactional
    public void deleteMarineByCoordinateForUser(Long coordinateId, Long userId) {
        spaceMarineRepository.deleteAllByCoordinatesIdAndUserId(coordinateId, userId);
    }

    public long countMarines() {
        return spaceMarineRepository.count();
    }
}
