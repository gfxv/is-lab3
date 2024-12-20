package dev.gfxv.lab3.repository;

import dev.gfxv.lab3.dao.CoordinatesDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordinateRepository extends JpaRepository<CoordinatesDAO, Long> {
    boolean existsByXAndY(Integer x, Long y);
    Optional<CoordinatesDAO> findByXAndY(Integer x, Long y);
}
