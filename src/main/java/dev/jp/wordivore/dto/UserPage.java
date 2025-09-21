package dev.jp.wordivore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserPage {
    private List<AppUserSummaryDto> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;

}
