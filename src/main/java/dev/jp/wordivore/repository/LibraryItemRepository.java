package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.LibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {
    List<LibraryItem> findAllByAppUser_Id(Long id);
    List<LibraryItem> findTop3ByAppUser_IdOrderByCreatedAtDesc(Long id);

    boolean existsByAppUser_IdAndEdition_Id(Long userId, Long editionId);
}
