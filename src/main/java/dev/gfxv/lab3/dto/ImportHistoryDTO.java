package dev.gfxv.lab3.dto;

import dev.gfxv.lab3.dao.ImportHistoryDAO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportHistoryDTO {

    Long id;
    Integer rowsAdded;
    String status;
    UserDTO user;
    String minioFilename;

    public static ImportHistoryDTO fromDAO(ImportHistoryDAO importHistoryDAO){
        return ImportHistoryDTO.builder()
                .id(importHistoryDAO.getId())
                .status(importHistoryDAO.getStatus())
                .rowsAdded(importHistoryDAO.getRowsAdded())
                .user(UserDTO.fromDAO(importHistoryDAO.getUser()))
                .minioFilename(importHistoryDAO.getMinioFilename())
                .build();
    }

    public static ImportHistoryDAO toDAO(ImportHistoryDTO dto) {
        ImportHistoryDAO dao = new ImportHistoryDAO();
        if (dto.getId() != null) {
            dao.setId(dto.getId());
        }
        dao.setStatus(dto.getStatus());
        dao.setRowsAdded(dto.getRowsAdded());
        if (dto.getMinioFilename() != null) {
            dao.setMinioFilename(dto.getMinioFilename());
        }
        return dao;
    }
}

