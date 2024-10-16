package dev.gfxv.lab1.repository;

import dev.gfxv.lab1.dao.AdminRequestDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRequestRepository extends JpaRepository<AdminRequestDAO, Long> {
}
