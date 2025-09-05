package dev.jp.wordivore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "edition_contributor",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_edition_contributor",
        columnNames = {"edition_id", "person_id"})
        },
    indexes = {
        @Index(name = "idx_edctbr_edition", columnList = "edition_id"),
        @Index(name = "idx_edctbr_person", columnList = "person_id"),
    }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditionContributor extends PersistedEntity {

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "edition_id", nullable = false)
    private Edition edition;
}
