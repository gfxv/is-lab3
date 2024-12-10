package dev.gfxv.lab2.repository;

import dev.gfxv.lab2.dao.AdminRequestDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRequestRepository extends JpaRepository<AdminRequestDAO, Long> {

    Optional<AdminRequestDAO> findByUser_Username(String username);
}
