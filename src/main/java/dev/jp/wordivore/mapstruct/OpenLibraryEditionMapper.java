package dev.jp.wordivore.mapstruct;

import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.model.Edition;
import dev.jp.wordivore.model.Work;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OpenLibraryEditionMapper {

    @Mapping(target = "subjects", ignore = true)
    OpenLibraryDto toOpenLibraryDto(Edition edition);

//    @Mapping(target = "work", ignore = true)
//    @Mapping(target = "publisher", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "version", ignore = true)
//    Edition toEditionEntity(OpenLibraryDto bookDto);


    default List<String> map(String value){
        return StringUtils.hasText(value) ? List.of(value) : Collections.emptyList();
    }

    default String map(List<String> value){
        return value.isEmpty() ? "" : value.getFirst();
    }
}
