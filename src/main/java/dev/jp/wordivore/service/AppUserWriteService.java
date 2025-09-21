package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.AppUserSummaryDto;
import dev.jp.wordivore.model.AppUser;
import dev.jp.wordivore.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserWriteService {

    private final AppUserRepository appUserRepository;

    public AppUserSummaryDto disable(String username){
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        appUser.setEnabled(false);
        AppUser updated = appUserRepository.save(appUser);

        return AppUserSummaryDto.builder()
            .id(updated.id)
                .enabled(updated.isEnabled())
                .username(username)
                .email(updated.getEmail())
                .build();
    }

    public AppUserSummaryDto enable(String username){
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username :" + username));

        appUser.setEnabled(true);
        AppUser updated = appUserRepository.save(appUser);

        return AppUserSummaryDto.builder()
                .id(updated.id)
                .enabled(updated.isEnabled())
                .username(username)
                .email(updated.getEmail())
                .build();
    }
}
