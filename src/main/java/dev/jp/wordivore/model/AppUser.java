package dev.jp.wordivore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AppUser extends PersistedEntity{
    private String username;
    private String password;
    private boolean enabled;
    private String roles;
    private String email;
    private int age;
    private String gender;
    private String bio;
}
