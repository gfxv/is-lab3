package dev.gfxv.lab2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.gfxv.lab2.dao.ChapterDAO;
import jakarta.validation.constraints.Min;
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
    @Min(value = 5, message = "Parent Legion must be at least 5 characters long")
    String parentLegion;
    Integer marinesCount;
    String world;

    @JsonCreator
    public static ChapterDTO create(
            @JsonProperty("name") String name,
            @JsonProperty("parentLegion") String parentLegion,
            @JsonProperty("marinesCount") Integer marinesCount,
            @JsonProperty("world") String world
    ) {
        return ChapterDTO.builder()
                .id(null)
                .name(name)
                .parentLegion(parentLegion)
                .marinesCount(marinesCount)
                .world(world)
                .build();
    }


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
