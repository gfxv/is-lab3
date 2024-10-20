package dev.gfxv.lab1.service;

import dev.gfxv.lab1.dao.AdminRequestDAO;
import dev.gfxv.lab1.dao.RoleDAO;
import dev.gfxv.lab1.dao.UserDAO;
import dev.gfxv.lab1.dto.AdminRequestDTO;
import dev.gfxv.lab1.dto.UserDTO;
import dev.gfxv.lab1.exceptions.NotFoundException;
import dev.gfxv.lab1.exceptions.RoleNotFoundException;
import dev.gfxv.lab1.exceptions.UserNotFoundException;
import dev.gfxv.lab1.repository.AdminRequestRepository;
import dev.gfxv.lab1.repository.RoleRepository;
import dev.gfxv.lab1.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {

    UserRepository userRepository;
    AdminRequestRepository adminRequestRepository;
    RoleRepository roleRepository;

    @Autowired
    public AdminService(
        UserRepository userRepository,
        AdminRequestRepository adminRequestRepository,
        RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.adminRequestRepository = adminRequestRepository;
        this.roleRepository = roleRepository;
    }

    public void newAdminRequest(String username) {
        UserDAO user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username '%s' not found", username)));

        AdminRequestDAO adminRequest = new AdminRequestDAO();
        adminRequest.setUser(user);

        adminRequestRepository.save(adminRequest);
    }

    public List<AdminRequestDTO> getPendingRequests() {
        return adminRequestRepository.findAll()
                .stream()
                .map(AdminRequestDTO::fromDAO)
                .toList();
    }

    public boolean isPendingUser(String username) {
        Optional<AdminRequestDAO> request = adminRequestRepository.findByUser_Username(username);
        return request.isPresent();
    }

    public void acceptRequest(Long id) throws RoleNotFoundException, NotFoundException {
        AdminRequestDAO request = adminRequestRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("SOMETHING WHEN TERRIBLY WRONG: username '%s' not found", id)));

        UserDAO user = request.getUser();
        RoleDAO adminRole = roleRepository
                .findByName("ADMIN")
                .orElseThrow(() -> new RoleNotFoundException(String.format("SOMETHING WHEN TERRIBLY WRONG: role '%s' not found", id)));

        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
        }
        userRepository.save(user);
        removePendingRequest(id);
    }

    public void denyRequest(Long id) throws NotFoundException {
        if (!adminRequestRepository.existsById(id)) {
            throw new NotFoundException(String.format("Request with id %d not found", id));
        }
        removePendingRequest(id);
    }

    private void removePendingRequest(Long id) {
        adminRequestRepository.deleteById(id);
    }
}
