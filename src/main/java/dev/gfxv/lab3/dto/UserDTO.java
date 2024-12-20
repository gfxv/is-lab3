package dev.gfxv.lab3.dto;

import dev.gfxv.lab3.dao.RoleDAO;
import dev.gfxv.lab3.dao.UserDAO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;
    String username;
    List<String> roles; // ?

    public static UserDTO fromDAO(UserDAO userDAO) {
        return UserDTO.builder()
                .id(userDAO.getId())
                .username(userDAO.getUsername())
                .roles(userDAO.getRoles().stream()
                        .map(RoleDAO::getName).toList())
                .build();
    }
}
