package dev.jp.emancipate_the_self.service;

import dev.jp.emancipate_the_self.dto.BookDto;
import dev.jp.emancipate_the_self.mapstruct.BookMapper;
import dev.jp.emancipate_the_self.model.AppUser;
import dev.jp.emancipate_the_self.model.Book;
import dev.jp.emancipate_the_self.repository.AppUserRepository;
import dev.jp.emancipate_the_self.repository.BookRepository;
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

    public List<Book> getUserLibrary(Long id){
        return bookRepository.findAllByAppUserId(id);
    }

    public void insertBook(BookDto bookDto, Long userId) {
        Book book = bookMapper.toEntity(bookDto);
        book.setAppUser(appUserRepository.getReferenceById(userId));
        bookRepository.save(book);
    }
}
