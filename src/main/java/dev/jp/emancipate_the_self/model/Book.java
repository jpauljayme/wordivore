package dev.jp.emancipate_the_self.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Book extends PersistedEntity {

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "authors",
    columnDefinition = "text[]",
    nullable = false)
    private String[] authors;

    @Column( name = "publication_date")
    private int publicationDate;

    @Column(name = "title", nullable = false)
    private String title;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "subjects",
            columnDefinition = "text[]",
            nullable = false)
    private String[] subjects;

    @Column(name = "pages", nullable = false)
    private int pages;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "isbn_10",
        columnDefinition = "text[]",
        nullable = false)
    private String[] isbn10;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser appUser;
}