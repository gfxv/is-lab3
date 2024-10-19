package dev.gfxv.lab1.dto;

import dev.gfxv.lab1.dao.ChapterDAO;
import dev.gfxv.lab1.dao.CoordinatesDAO;
import dev.gfxv.lab1.dao.SpaceMarineDAO;
import dev.gfxv.lab1.dao.enums.MeleeWeapon;
import dev.gfxv.lab1.dao.enums.Weapon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceMarineDTO {
    String name;
    LocalDate creationDate;
    Integer health;
    Long height;
    Weapon weapon;
    MeleeWeapon meleeWeapon;
    CoordinatesDTO coordinates;
    ChapterDTO chapter;

    public static SpaceMarineDTO fromDAO(SpaceMarineDAO spaceMarineDAO) {
        return SpaceMarineDTO
                .builder()
                .name(spaceMarineDAO.getName())
                .creationDate(spaceMarineDAO.getCreationDate())
                .health(spaceMarineDAO.getHealth())
                .height(spaceMarineDAO.getHeight())
                .weapon(spaceMarineDAO.getWeapon())
                .meleeWeapon(spaceMarineDAO.getMeleeWeapon())
                .coordinates(CoordinatesDTO.fromDAO(spaceMarineDAO.getCoordinates()))
                .chapter(ChapterDTO.fromDAO(spaceMarineDAO.getChapter()))
                .build();
    }

    public static SpaceMarineDAO toDAO(SpaceMarineDTO dto) {
        SpaceMarineDAO dao = new SpaceMarineDAO();
        dao.setName(dto.getName());
        dao.setCreationDate(dto.getCreationDate());
        dao.setHealth(dto.getHealth());
        dao.setHeight(dto.getHeight());
        dao.setWeapon(dto.getWeapon());
        dao.setMeleeWeapon(dto.getMeleeWeapon());
        dao.setCoordinates(CoordinatesDTO.toDAO(dto.getCoordinates()));
        dao.setChapter(ChapterDTO.toDAO(dto.getChapter()));
        return dao;
    }
}
