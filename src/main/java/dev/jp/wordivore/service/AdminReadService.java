package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.AppUserSummaryDto;
import dev.jp.wordivore.dto.UserPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReadService {
    private final AppUserReadService appUserService;
    
    public UserPage paginatedAppUsers(int page){
        Page<AppUserSummaryDto> list = appUserService.list(page);

        return UserPage.builder()
                .totalElements(list.getTotalElements())
                .content(list.getContent())
                .currentPage(list.getNumber())
                .totalPages(list.getTotalPages())
                .build();
    }
}
