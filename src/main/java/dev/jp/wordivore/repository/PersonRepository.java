package dev.jp.wordivore.repository;

import dev.jp.wordivore.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByName(@Param("name") String name);

    Optional<Person> findByName(@Param("name") String name);
}
