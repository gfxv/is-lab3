package dev.gfxv.lab3.repository;

import dev.gfxv.lab3.dao.SpaceMarineDAO;
import dev.gfxv.lab3.dao.enums.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceMarineRepository extends JpaRepository<SpaceMarineDAO, Long> {
    List<SpaceMarineDAO> findAllByUserUsername(String name);
    List<SpaceMarineDAO> findAllByWeapon(Weapon weapon);
    void deleteAllByChapterIdAndUserId(Long chapterId, Long userId);
    void deleteAllByCoordinatesIdAndUserId(Long coordinatesId, Long userId);
    int countAllByHeight(Long height);
}
