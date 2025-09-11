package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    Optional<Work> findByTitle(@Param("title") String title);

    boolean existsByTitle(@Param("title") String title);

    Optional<Work> findByKey(String key);

}
