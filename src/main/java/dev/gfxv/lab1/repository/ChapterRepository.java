package dev.gfxv.lab1.repository;

import dev.gfxv.lab1.dao.ChapterDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterDAO, Long> {
}
