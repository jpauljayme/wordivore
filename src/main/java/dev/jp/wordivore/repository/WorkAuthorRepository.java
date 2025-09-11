package dev.jp.wordivore.repository;

import dev.jp.wordivore.dto.AuthorDto;
import dev.jp.wordivore.model.WorkAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WorkAuthorRepository extends JpaRepository<WorkAuthor, Long> {

    boolean findByPerson_NameIn(List<String> names);

    boolean existsByWork_IdAndPerson_Id(Long workId, Long personId);


    @Query("""
            SELECT new dev.jp.wordivore.dto.AuthorDto(
                wa.work.id,
                wa.person.name
            )
                FROM WorkAuthor wa
                WHERE wa.work.id IN :workIds
                ORDER BY wa.work.id
            """)
    //Add position column
    List<AuthorDto> findRowsByWork_Id(Collection<Long> workIds);
}
