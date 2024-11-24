package dev.gfxv.lab1.repository;

import dev.gfxv.lab1.dao.ChapterDAO;
import dev.gfxv.lab1.dao.SpaceMarineDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceMarineRepository extends JpaRepository<SpaceMarineDAO, Long> {
    List<SpaceMarineDAO> findAllByUserUsername(String name);
}
