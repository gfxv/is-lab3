package dev.gfxv.lab2.dto;

import dev.gfxv.lab2.dao.EditHistoryDAO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditHistoryDTO {

    Long id;
    SpaceMarineDTO marine;
    LocalDate editDate;
    UserDTO user;

    public static EditHistoryDTO fromDAO(EditHistoryDAO editHistoryDAO){
        return EditHistoryDTO.builder()
                .id(editHistoryDAO.getId())
                .marine(SpaceMarineDTO.fromDAO(editHistoryDAO.getMarine()))
                .editDate(editHistoryDAO.getEditDate())
                .user(UserDTO.fromDAO(editHistoryDAO.getUser()))
                .build();
    }

    public static EditHistoryDAO toDAO(EditHistoryDTO dto) {
        EditHistoryDAO dao = new EditHistoryDAO();
        if (dto.getId() != null) {
            dao.setId(dto.getId());
        }
        dao.setMarine(SpaceMarineDTO.toDAO(dto.getMarine()));
        dao.setEditDate(dto.getEditDate());
        return dao;
    }
}

