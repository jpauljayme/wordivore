package dev.jp.wordivore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import software.amazon.awssdk.annotations.NotNull;

@Entity
@Table(name = "person",
uniqueConstraints = @UniqueConstraint(name = "uk_person_name", columnNames = {"name"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Person extends PersistedEntity{

    @Column(name = "name")
    String name;
}
