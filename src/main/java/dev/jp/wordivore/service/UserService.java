package dev.jp.wordivore.service;

import dev.jp.wordivore.model.AppUser;
import dev.jp.wordivore.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AppUserRepository userRepository;

    public Optional<AppUser> getUser(String username){
        return userRepository.findByUsername(username);
    }
}
