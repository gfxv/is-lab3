package dev.gfxv.lab2.dto;

import dev.gfxv.lab2.dao.ChapterDAO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterDTO {
    Long id;
    String name;
    String parentLegion;
    Integer marinesCount;
    String world;

    public static ChapterDTO fromDAO(ChapterDAO dao) {
        return ChapterDTO.builder()
                .id(dao.getId())
                .name(dao.getName())
                .parentLegion(dao.getParentLegion())
                .marinesCount(dao.getMarinesCount())
                .world(dao.getWorld())
                .build();
    }

    public static ChapterDAO toDAO(ChapterDTO dto) {
        ChapterDAO dao = new ChapterDAO();
        if (dto.getId() != null) {
            dao.setId(dto.getId());
        }
        dao.setName(dto.getName());
        dao.setParentLegion(dto.getParentLegion());
        dao.setMarinesCount(dto.getMarinesCount());
        dao.setWorld(dto.getWorld());
        return dao;
    }
}
