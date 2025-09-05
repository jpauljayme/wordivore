package dev.jp.wordivore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "edition_translator",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_edition_translator",
        columnNames = {"edition_id", "person_id"})
    },
    indexes = {
        @Index(name = "idx_edtr_edition", columnList = "edition_id"),
        @Index(name = "idx_edtr_person", columnList = "person_id")
    }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditionTranslator extends PersistedEntity {

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "edition_id", nullable = false)
    private Edition edition;
}
