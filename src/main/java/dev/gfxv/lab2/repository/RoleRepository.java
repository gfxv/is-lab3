package dev.gfxv.lab2.repository;

import dev.gfxv.lab2.dao.RoleDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleDAO, Long> {
    Optional<RoleDAO> findByName(String name);
}
