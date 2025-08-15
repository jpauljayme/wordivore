package dev.jp.emancipate_the_self.mapstruct;

import dev.jp.emancipate_the_self.dto.BookDto;
import dev.jp.emancipate_the_self.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "appUser", ignore = true)
    Book toEntity(BookDto bookDto);

    default List<String> map(String[] arr) {
        return (arr == null ) ? List.of() : Arrays.asList(arr);
    }

    default String[] map(List<String> list) {
        return (list == null || list.isEmpty()) ? new String[0] : list.toArray(new String[0]);
    }
}
