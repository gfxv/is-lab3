package dev.gfxv.lab1.service;

import dev.gfxv.lab1.dto.ChapterDTO;
import dev.gfxv.lab1.repository.ChapterRepository;
import dev.gfxv.lab1.repository.SpaceMarineRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterService {

    ChapterRepository chapterRepository;
    SpaceMarineRepository spaceMarineRepository;

    @Autowired
    public ChapterService(
        ChapterRepository chapterRepository,
        SpaceMarineRepository spaceMarineRepository
    ) {
        this.chapterRepository = chapterRepository;
        this.spaceMarineRepository = spaceMarineRepository;
    }

    public List<ChapterDTO> getAllChapters() {
        return chapterRepository.findAll()
                .stream()
                .map(ChapterDTO::fromDAO)
                .toList();
    }

    public List<ChapterDTO> getChaptersByUser(String username) {
        return spaceMarineRepository.findAllByUserUsername(username)
                .stream()
                .map(dao -> ChapterDTO.fromDAO(dao.getChapter()))
                .distinct()
                .toList();
    }
}
