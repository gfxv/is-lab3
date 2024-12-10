package dev.gfxv.lab2.dto;

import dev.gfxv.lab2.dao.RoleDAO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {
    String name;

    public static RoleDTO fromDAO(RoleDAO dao) {
        return RoleDTO.builder()
                .name(dao.getName())
                .build();
    }

    public static RoleDAO toDAO(RoleDTO dto) {
        RoleDAO dao = new RoleDAO();
        dao.setName(dto.getName());
        return dao;
    }
}

