package dev.gfxv.lab1.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "admin_requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminRequestDAO {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin-requests-sequence-generator"
    )
    @SequenceGenerator(
            name = "admin-requests-sequence-generator",
            sequenceName = "admin_requests_id_seq",
            allocationSize=1
    )
    Long id;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserDAO user;
}
