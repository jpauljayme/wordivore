package dev.jp.emancipate_the_self.repository;

import dev.jp.emancipate_the_self.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByAppUserId(Long id);
}
