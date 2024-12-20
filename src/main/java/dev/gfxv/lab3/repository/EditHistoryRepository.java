package dev.gfxv.lab3.repository;

import dev.gfxv.lab3.dao.EditHistoryDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditHistoryRepository extends JpaRepository<EditHistoryDAO, Long> {
    List<EditHistoryDAO> findAllByMarineId(Long marineId);
}
