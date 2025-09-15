package dev.jp.wordivore.service;

import dev.jp.wordivore.model.AppUser;
import dev.jp.wordivore.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;


    public AppUser getAppUserDetails(Long id){
        return appUserRepository.getReferenceById(id);
    }
}
