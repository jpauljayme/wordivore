package dev.jp.wordivore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "work_author")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WorkAuthor extends PersistedEntity {

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    Person person;

    @ManyToOne
    @JoinColumn(name = "work_id", nullable = false)
    Work work;
}
