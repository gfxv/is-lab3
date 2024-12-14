package dev.gfxv.lab2.repository;

import dev.gfxv.lab2.dao.ImportHistoryDAO;
import dev.gfxv.lab2.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportHistoryRepository  extends JpaRepository<ImportHistoryDAO, Long> {
    List<ImportHistoryDAO> findAllByUser(UserDAO user);
}
