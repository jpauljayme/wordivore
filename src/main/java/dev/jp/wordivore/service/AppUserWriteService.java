package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.AppUserSummaryDto;
import dev.jp.wordivore.model.AppUser;
import dev.jp.wordivore.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserWriteService {

    private final AppUserRepository appUserRepository;

    public void disable(String username){
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        appUser.setEnabled(false);
        appUserRepository.save(appUser);
    }

    public void enable(String username){
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username :" + username));

        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
}
