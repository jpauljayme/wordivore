package dev.jp.emancipate_the_self.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
//@Table(name = "users")
@Getter
@AllArgsConstructor
public class User {
    @Id
    private int id;
    private String username;
    private String password;
    private String roles;
}
