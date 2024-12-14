package dev.gfxv.lab2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.gfxv.lab2.dao.SpaceMarineDAO;
import dev.gfxv.lab2.dao.enums.MeleeWeapon;
import dev.gfxv.lab2.dao.enums.Weapon;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceMarineDTO {
    Long id;
    String name;

    @Past(message = "Creation Date must be in the past")
    LocalDate creationDate;

    Integer health;

    @Max(value = 250, message = "Marine's height must be less than 250")
    Long height;

    Weapon weapon;
    MeleeWeapon meleeWeapon;

    CoordinatesDTO coordinates;
    @Valid
    ChapterDTO chapter;
    UserDTO owner;

    @JsonCreator
    public static SpaceMarineDTO create(
            @JsonProperty("name") String name,
            @JsonProperty("creationDate") LocalDate creationDate,
            @JsonProperty("health") Integer health,
            @JsonProperty("height") Long height,
            @JsonProperty("weapon") Weapon weapon,
            @JsonProperty("meleeWeapon") MeleeWeapon meleeWeapon,
            @JsonProperty("coordinates") CoordinatesDTO coordinates,
            @JsonProperty("chapter") ChapterDTO chapter) {
        return SpaceMarineDTO.builder()
                .id(null)
                .name(name)
                .creationDate(creationDate)
                .health(health)
                .height(height)
                .weapon(weapon)
                .meleeWeapon(meleeWeapon)
                .coordinates(coordinates)
                .chapter(chapter)
                .owner(null)
                .build();
    }

    public static SpaceMarineDTO fromDAO(SpaceMarineDAO spaceMarineDAO) {
        return SpaceMarineDTO
                .builder()
                .id(spaceMarineDAO.getId())
                .name(spaceMarineDAO.getName())
                .creationDate(spaceMarineDAO.getCreationDate())
                .health(spaceMarineDAO.getHealth())
                .height(spaceMarineDAO.getHeight())
                .weapon(spaceMarineDAO.getWeapon())
                .meleeWeapon(spaceMarineDAO.getMeleeWeapon())
                .coordinates(CoordinatesDTO.fromDAO(spaceMarineDAO.getCoordinates()))
                .chapter(ChapterDTO.fromDAO(spaceMarineDAO.getChapter()))
                .owner(UserDTO.fromDAO(spaceMarineDAO.getUser()))
                .build();
    }

    public static SpaceMarineDAO toDAO(SpaceMarineDTO dto) {
        SpaceMarineDAO dao = new SpaceMarineDAO();
        if (dto.getId() != null) {
            dao.setId(dto.getId());
        }
        dao.setName(dto.getName());
        if (dto.getCreationDate() != null) {
            dao.setCreationDate(dto.getCreationDate());
        } else {
            dao.setCreationDate(LocalDate.now());
        }
        dao.setHealth(dto.getHealth());
        dao.setHeight(dto.getHeight());
        dao.setWeapon(dto.getWeapon());
        dao.setMeleeWeapon(dto.getMeleeWeapon());
        dao.setCoordinates(CoordinatesDTO.toDAO(dto.getCoordinates()));
        dao.setChapter(ChapterDTO.toDAO(dto.getChapter()));
        return dao;
    }
}
