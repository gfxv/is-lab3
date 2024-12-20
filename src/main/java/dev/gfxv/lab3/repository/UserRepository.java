package dev.gfxv.lab3.repository;

import dev.gfxv.lab3.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDAO, Long> {
    Optional<UserDAO> findByUsername(String username);
    Boolean existsByUsername(String username);
}
