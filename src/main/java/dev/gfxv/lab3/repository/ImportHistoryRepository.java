package dev.gfxv.lab3.repository;

import dev.gfxv.lab3.dao.ImportHistoryDAO;
import dev.gfxv.lab3.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportHistoryRepository  extends JpaRepository<ImportHistoryDAO, Long> {
    List<ImportHistoryDAO> findAllByUser(UserDAO user);
}
