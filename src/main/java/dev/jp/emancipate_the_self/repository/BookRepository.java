package dev.jp.emancipate_the_self.repository;

import dev.jp.emancipate_the_self.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where b.appUser.id = :id")
    List<Book> findAllByAppUser_Id(Long id);

}
