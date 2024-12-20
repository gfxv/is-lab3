package dev.gfxv.lab3.repository;

import dev.gfxv.lab3.dao.ChapterDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterDAO, Long> {
    Optional<ChapterDAO> findByNameAndParentLegionAndMarinesCountAndWorld(
            String name,
            String parentLegion,
            Integer marinesCount,
            String world
    );
    Optional<ChapterDAO> findByName(String name);
}
