package dev.gfxv.lab2.dto;

import dev.gfxv.lab2.dao.AdminRequestDAO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminRequestDTO {

    Long id;
    UserDTO user;

    public static AdminRequestDTO fromDAO(AdminRequestDAO dao) {
        return AdminRequestDTO.builder()
                .id(dao.getId())
                .user(UserDTO.fromDAO(dao.getUser()))
                .build();
    }
}
