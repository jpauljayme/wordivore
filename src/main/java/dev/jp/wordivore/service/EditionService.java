package dev.jp.wordivore.service;

import dev.jp.wordivore.model.Edition;
import dev.jp.wordivore.repository.EditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EditionService {
    private final EditionRepository editionRepository;

    public Optional<Edition> findByIsbn10(String isbn10) {
        return editionRepository.findByIsbn10(isbn10);
    }

    public Optional<Edition> findByIsbn13(String isbn13) {
        return editionRepository.findByIsbn13(isbn13);
    }
}
