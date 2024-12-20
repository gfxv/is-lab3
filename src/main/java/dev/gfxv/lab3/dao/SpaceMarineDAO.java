package dev.gfxv.lab3.dao;

import dev.gfxv.lab3.dao.enums.MeleeWeapon;
import dev.gfxv.lab3.dao.enums.Weapon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "space_marine")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceMarineDAO {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "space-marine-sequence-generator"
    )
    @SequenceGenerator(
            name = "space-marine-sequence-generator",
            sequenceName = "space_marine_id_seq",
            allocationSize=1
    )
    Long id;

    @Column(columnDefinition = "NOT NULL CHECK ( length(name) > 0 )")
    String name;

    @Column(name = "creation_date", nullable = false)
    LocalDate creationDate;

    @Column(columnDefinition = "CHECK ( health > 0 )")
    Integer health;

    @Column(nullable = false)
    Long height;

    @Column(name = "weapon")
    @Enumerated(EnumType.STRING)
    Weapon weapon;

    @Column(name = "melee_weapon")
    @Enumerated(EnumType.STRING)
    MeleeWeapon meleeWeapon;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    CoordinatesDAO coordinates;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "chapter_id", referencedColumnName = "id")
    ChapterDAO chapter;

    @ManyToOne
    @JoinColumn(name="owner_id", referencedColumnName = "id")
    UserDAO user;
}
