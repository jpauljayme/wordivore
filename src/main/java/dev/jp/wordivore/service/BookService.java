package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.BookDto;
import dev.jp.wordivore.exception.BookDuplicateIsbnException;
import dev.jp.wordivore.mapstruct.BookMapper;
import dev.jp.wordivore.model.Book;
import dev.jp.wordivore.repository.AppUserRepository;
import dev.jp.wordivore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AppUserRepository appUserRepository;

    public List<Book> getUserLibrary(Long id) {
        return bookRepository.findAllByAppUser_Id(id);
    }

    public List<Book> getUserLibraryMostRecent(Long id) {
        return bookRepository.findTop3ByAppUser_IdOrderByCreatedAtDesc(id);
    }

    public void insertBook(BookDto bookDto, String isbn, Long userId) throws BookDuplicateIsbnException {
        if (bookRepository.existsByIsbn10(isbn)) {
            return;
        }

        Book book = bookMapper.toEntity(bookDto);

        book.setAppUser(appUserRepository.getReferenceById(userId));
        bookRepository.save(book);
    }
}
