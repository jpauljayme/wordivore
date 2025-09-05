package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.WorkAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkAuthorRepository extends JpaRepository<WorkAuthor, Long> {

    boolean findByPerson_NameIn(List<String> names);

    boolean existsByWork_IdAndPerson_Id(Long workId, Long personId);

}
