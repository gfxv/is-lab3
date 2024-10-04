package dev.gfxv.lab1.repository;

import dev.gfxv.lab1.dao.RoleDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleDAO, Long> {
    Optional<RoleDAO> findByName(String name);
}
