package dev.gfxv.lab2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.gfxv.lab2.dao.CoordinatesDAO;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoordinatesDTO {
    Long id;
    Integer x;
    Long y;

    @JsonCreator
    public static CoordinatesDTO create(
            @JsonProperty("x") Integer x,
            @JsonProperty("y") Long y
    ) {
        return CoordinatesDTO.builder()
                .id(null)
                .x(x)
                .y(y)
                .build();
    }

    public static CoordinatesDTO fromDAO(CoordinatesDAO coordinatesDAO){
        return CoordinatesDTO.builder()
                .id(coordinatesDAO.getId())
                .x(coordinatesDAO.getX())
                .y(coordinatesDAO.getY())
                .build();
    }

    public static CoordinatesDAO toDAO(CoordinatesDTO dto) {
        CoordinatesDAO dao = new CoordinatesDAO();
        if (dto.getId() != null) {
            dao.setId(dto.getId());
        }
        dao.setX(dto.getX());
        dao.setY(dto.getY());
        return dao;
    }
}
