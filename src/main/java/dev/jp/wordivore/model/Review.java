package dev.jp.wordivore.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends PersistedEntity{

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private BigDecimal rating;

    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edition_id",
    referencedColumnName = "id",
            nullable = false
    )
    private Edition edition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id",
    referencedColumnName = "id")
    private Work work;
}
