package dev.jp.emancipate_the_self.service;

import dev.jp.emancipate_the_self.model.AppUser;
import dev.jp.emancipate_the_self.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<AppUser> getUser(String username){
        return userRepository.findByUsername(username);
    }
}
