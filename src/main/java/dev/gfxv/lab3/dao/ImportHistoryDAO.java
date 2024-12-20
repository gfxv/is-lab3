package dev.gfxv.lab3.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "import_history")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportHistoryDAO {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "import-history-sequence-generator"
    )
    @SequenceGenerator(
            name = "import-history-sequence-generator",
            sequenceName = "import_history_id_seq",
            allocationSize=1
    )
    Long id;

    @Column(name="rows_added", nullable = false)
    Integer rowsAdded;

    @Column(nullable = false)
    String status;

    @ManyToOne
    @JoinColumn(name="imported_by", referencedColumnName = "id")
    UserDAO user;
}
