package dev.gfxv.lab3.service;

import dev.gfxv.lab3.dao.ImportHistoryDAO;
import dev.gfxv.lab3.dao.UserDAO;
import dev.gfxv.lab3.dto.ImportHistoryDTO;
import dev.gfxv.lab3.dto.SpaceMarineDTO;
import dev.gfxv.lab3.exceptions.InvalidArgumentException;
import dev.gfxv.lab3.exceptions.UserNotFoundException;
import dev.gfxv.lab3.repository.ImportHistoryRepository;
import dev.gfxv.lab3.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.MethodInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImportService {

    SpaceMarineService spaceMarineService;
    ImportHistoryRepository importHistoryRepository;
    UserRepository userRepository;

    @Autowired
    public ImportService(
            SpaceMarineService spaceMarineService,
            ImportHistoryRepository importHistoryRepository,
            UserRepository userRepository
    ) {
        this.spaceMarineService = spaceMarineService;
        this.importHistoryRepository = importHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = {
            UsernameNotFoundException.class,
            InvalidArgumentException.class,
            MethodInvocationException.class,
            ConstraintViolationException.class
    })
    public void importMarines(List<SpaceMarineDTO> marines, String owner) throws UserNotFoundException {
        for (var marine : marines) {
            spaceMarineService.newMarine(marine, owner);
        }
        UserDAO user = userRepository.findByUsername(owner)
                .orElseThrow(() -> new UserNotFoundException("username not found"));
        ImportHistoryDAO importHistory = new ImportHistoryDAO();
        importHistory.setUser(user);
        importHistory.setRowsAdded(marines.size());
        importHistory.setStatus("COMPLETE");
        importHistoryRepository.save(importHistory);
    }

    public List<ImportHistoryDTO> getALLImportHistory() {
        return importHistoryRepository.findAll().stream()
                .map(ImportHistoryDTO::fromDAO)
                .toList();
    }

    public List<ImportHistoryDTO> getImportHistoryByUser(String username) {
        UserDAO user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return importHistoryRepository.findAllByUser(user).stream()
                .map(ImportHistoryDTO::fromDAO)
                .toList();
    }
}
