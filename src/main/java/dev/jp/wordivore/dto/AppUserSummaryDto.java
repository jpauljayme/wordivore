package dev.jp.wordivore.dto;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AppUserSummaryDto {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
}
