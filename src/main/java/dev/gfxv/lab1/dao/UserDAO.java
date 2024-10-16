package dev.gfxv.lab1.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDAO {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user-sequence-generator"
    )
    @SequenceGenerator(
            name = "user-sequence-generator",
            sequenceName = "users_id_seq",
            allocationSize=1
    )
    Long id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = false)
    String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    List<RoleDAO> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<SpaceMarineDAO> spaceMarines;
}
