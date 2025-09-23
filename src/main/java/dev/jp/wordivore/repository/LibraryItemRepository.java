package dev.jp.wordivore.repository;

import dev.jp.wordivore.dto.AuthorDto;
import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.model.LibraryItem;
import dev.jp.wordivore.model.ShelfStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {

    @EntityGraph(attributePaths = {"edition", "edition.work"})
    List<LibraryItem> findAllByAppUser_Id(Long id);

    @EntityGraph(attributePaths = {"edition", "edition.work"})
    List<LibraryItem> findAllByAppUser_IdAndStatus(Long userId, ShelfStatus status);

    boolean existsByAppUser_IdAndEdition_Id(Long userId, Long editionId);


    @EntityGraph(attributePaths = {"edition", "edition.work"})
    Optional<LibraryItem> findWithEditionAndWorkById(@Param("id") Long id);
}
