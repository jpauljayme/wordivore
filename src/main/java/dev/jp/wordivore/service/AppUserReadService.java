package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.AppUserSummaryDto;
import dev.jp.wordivore.model.AppUser;
import dev.jp.wordivore.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserReadService {

    private final AppUserRepository appUserRepository;

    public Optional<AppUser> findAppUserByUsername(String username){
        return appUserRepository.findByUsername(username);
    }

    public Page<AppUserSummaryDto> list(int page){
        return appUserRepository.findAll(PageRequest.of(page, 5)).map(appUser -> AppUserSummaryDto.builder()
                .email(appUser.getEmail())
                .username(appUser.getUsername())
                .enabled(appUser.isEnabled())
            .build());
    }


}
