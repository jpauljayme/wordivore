package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.EditionContributor;
import dev.jp.wordivore.model.WorkAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditionContributorRepository extends JpaRepository<EditionContributor, Long> {

}
