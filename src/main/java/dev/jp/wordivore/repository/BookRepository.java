package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where b.appUser.id = :id")
    List<Book> findAllByAppUser_Id(Long id);

    @Query(value = "SELECT exists (SELECT 1 FROM book " +
            "WHERE :isbn = ANY (isbn_10))",
            nativeQuery = true
    )
    boolean existsByIsbn10(@Param("isbn") String isbn);
}
