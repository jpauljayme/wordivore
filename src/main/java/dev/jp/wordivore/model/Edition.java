package dev.jp.wordivore.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "edition")
@Table(name = "edition",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_edition_isbn10", columnNames = "isbn_10"),
        @UniqueConstraint(name = "uk_edition_isbn13", columnNames = "isbn_13")
    }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Edition extends PersistedEntity{

    @Column(name = "by_statement")
    private String byStatement;

    @Column(name = "title")
    private String title;

    @Column(name = "isbn_10",
    length = 10
    )
    private String isbn10;

    @Column(name = "isbn_13",
        length = 13
    )
    private String isbn13;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "cover_key")
    private String coverKey;

    @Type(ListArrayType.class)
    @Column(name = "publishers")
    private List<String> publishers;

    @Column(name = "publication_date")
    private Integer publicationDate;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "edition_name")
    private String editionName;

   @ManyToOne(fetch = FetchType.LAZY,
   optional = false)
   @JoinColumn(name = "work_id", nullable = false)
    private Work work;

   @Version
   @Column(nullable = false)
    private Long version;

}
