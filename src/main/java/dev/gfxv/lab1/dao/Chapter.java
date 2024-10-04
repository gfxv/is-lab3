package dev.gfxv.lab1.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "chapter")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chapter {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chapter-sequence-generator"
    )
    @SequenceGenerator(
            name = "chapter-sequence-generator",
            sequenceName = "chapter_id_seq",
            allocationSize=1
    )
    Long id;

    @Column(columnDefinition = "NOT NULL CHECK (name <> '')")
    String name;

    @Column(name = "parent_legion")
    String parentLegion;

    @Column(name = "marines_count", columnDefinition = "NOT NULL CHECK (marines_count > 0 AND marines_count < 1000)")
    Integer marinesCount;

    @Column(nullable = false)
    String world;
}
