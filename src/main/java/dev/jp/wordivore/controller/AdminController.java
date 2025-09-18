package dev.jp.wordivore.controller;

import dev.jp.wordivore.common.admin.AdminEndpoint;
import dev.jp.wordivore.dto.AppUserSummaryDto;
import dev.jp.wordivore.dto.UserPage;
import dev.jp.wordivore.service.AdminReadService;
import dev.jp.wordivore.service.AppUserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@AdminEndpoint
public class AdminController {
    private final AdminReadService adminReadService;
    private final AppUserWriteService appUserWriteService;


    @GetMapping("")
    public String dashboard(Model model,
                            @RequestHeader(value = "Hx-Request", defaultValue = "false") boolean isHxRequest,
                            @RequestParam(defaultValue = "0") int page){

        UserPage userPage = adminReadService.paginatedAppUsers(page);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

        return isHxRequest ? "fragments/admin :: usersTable" : "index";
    }

    @PostMapping("/user/enable")
    public String enableUser(Model model, String username){

        AppUserSummaryDto user = appUserWriteService.enable(username);

        model.addAttribute("u", user);
        return "fragments/admin :: userDetailsRow";
    }

    @PostMapping("/user/disable")
    public String disableUser(Model model, String username){

        AppUserSummaryDto user = appUserWriteService.disable(username);

        model.addAttribute("u", user);
        return "fragments/admin :: userDetailsRow";
    }



}
