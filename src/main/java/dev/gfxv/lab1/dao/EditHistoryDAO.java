package dev.gfxv.lab1.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "edit_history")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditHistoryDAO {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "edit-history-sequence-generator"
    )
    @SequenceGenerator(
            name = "edit-history-sequence-generator",
            sequenceName = "edit_history_id_seq",
            allocationSize=1
    )
    Long id;

    @ManyToOne
    @JoinColumn(name="marine_id", referencedColumnName = "id")
    SpaceMarineDAO marine;

    @Column(name = "edit_date", nullable = false)
    LocalDate editDate;

    @ManyToOne
    @JoinColumn(name="edited_by", referencedColumnName = "id")
    UserDAO user;
}
