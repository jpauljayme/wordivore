package dev.jp.wordivore.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Work extends PersistedEntity {


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "key")
    private String key;

    @Type(ListArrayType.class)
    @Column(name = "subjects",
            columnDefinition = "text[]",
            nullable = false)
    private List<String> subjects;

    @Column
    private String description;

    @Column(name = "first_sentence")
    private String firstSentence;


    @Column(name = "ave_rating",
    precision = 2,
    scale = 1)
    private BigDecimal averageRating;

    @Column(name = "ratings_count")
    private int ratingsCount;

    @Column(name = "reviews_count")
    private int reviewsCount;

    @Version
    @Column(nullable = false)
    private Long version;
}