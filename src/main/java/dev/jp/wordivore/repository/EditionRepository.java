package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.Edition;
import dev.jp.wordivore.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditionRepository extends JpaRepository<Edition, Long> {

//    @Query("select b from Book b where b.appUser.id = :id")
//    List<Work> findAllByAppUser_Id(Long id);

    boolean existsByIsbn10(@Param("isbn10") String isbn10);
    boolean existsByIsbn13(@Param("isbn13") String isbn13);

    Optional<Edition> findByIsbn10(@Param("isbn10") String isbn10);
    Optional<Edition> findByIsbn13(@Param("isbn13") String isbn13);
}
