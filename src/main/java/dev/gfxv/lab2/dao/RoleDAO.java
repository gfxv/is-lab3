package dev.gfxv.lab2.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDAO {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role-sequence-generator"
    )
    @SequenceGenerator(
            name = "role-sequence-generator",
            sequenceName = "role_id_seq",
            allocationSize=1
    )
    Long id;

    @Column(nullable = false, unique = true)
    String name;
}
