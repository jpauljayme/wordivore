package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.LibraryItem;
import dev.jp.wordivore.model.ShelfStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {

    @EntityGraph(attributePaths = {"edition", "edition.work"})
    List<LibraryItem> findAllByAppUser_Id(Long id);

    @EntityGraph(attributePaths = {"edition", "edition.work"})
    List<LibraryItem> findAllByAppUser_IdAndStatus(Long userId, ShelfStatus status);

    List<LibraryItem> findTop3ByAppUser_IdOrderByCreatedAtDesc(Long id);

    boolean existsByAppUser_IdAndEdition_Id(Long userId, Long editionId);

}
