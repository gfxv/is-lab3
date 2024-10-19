package dev.gfxv.lab1.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "coordinates")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoordinatesDAO {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coords-sequence-generator"
    )
    @SequenceGenerator(
            name = "coords-sequence-generator",
            sequenceName = "coordinates_id_seq",
            allocationSize=1
    )
    Long id;

    @Column(name = "x")
    Integer x;

    @Column(name = "y")
    Long y;

}
