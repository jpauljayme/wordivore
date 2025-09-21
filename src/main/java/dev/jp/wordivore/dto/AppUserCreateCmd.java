package dev.jp.wordivore.dto;

public record AppUserCreateCmd(String username,
String password,
String email,
String rawPassword) {
}
