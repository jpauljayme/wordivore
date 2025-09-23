
package dev.jp.wordivore.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;


@Entity
@Table(name = "library_item",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_edition_id", columnNames = {"user_id", "edition_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LibraryItem extends PersistedEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "edition_id", nullable = false)
    Edition edition;

    @Enumerated(value = EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status",
    columnDefinition = "shelf_status",
    nullable = false)
    ShelfStatus status;

    @Column(name = "read_start")
    LocalDate readStart;

    @Column(name = "read_end")
    LocalDate readEnd;
}
