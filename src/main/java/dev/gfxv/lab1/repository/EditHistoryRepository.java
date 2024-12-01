package dev.gfxv.lab1.repository;

import dev.gfxv.lab1.dao.EditHistoryDAO;
import dev.gfxv.lab1.dao.RoleDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditHistoryRepository extends JpaRepository<EditHistoryDAO, Long> {
    List<EditHistoryDAO> findAllByMarineId(Long marineId);
}
