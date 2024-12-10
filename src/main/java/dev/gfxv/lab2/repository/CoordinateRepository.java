package dev.gfxv.lab2.repository;

import dev.gfxv.lab2.dao.CoordinatesDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordinateRepository extends JpaRepository<CoordinatesDAO, Long> {
    boolean existsByXAndY(Integer x, Long y);
    Optional<CoordinatesDAO> findByXAndY(Integer x, Long y);
}
