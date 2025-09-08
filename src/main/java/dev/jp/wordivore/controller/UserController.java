package dev.jp.wordivore.controller;

import dev.jp.wordivore.model.LibraryItem;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.service.LibraryItemService;
import dev.jp.wordivore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LibraryItemService libraryItemService;

    @Value("${CLOUDFRONT_URL}")
    private String prefix;

    @GetMapping("/user")
    public String getUser(Model model, @AuthenticationPrincipal SecurityUser securityUser){

        model.addAttribute("username", securityUser.getUsername());
        model.addAttribute("prefix", prefix);

        List<LibraryItem> userLib = libraryItemService.getUserLibrary(securityUser.getUserId());
        model.addAttribute("books",  userLib);
        model.addAttribute("libCount", userLib.size());
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal SecurityUser securityUser){

        model.addAttribute("username", securityUser.getUsername());

        List<LibraryItem> recentFour = libraryItemService.getUserLibrary(securityUser.getUserId())
                .stream()
                .sorted(Collections.reverseOrder())
                .limit(4).toList();
        model.addAttribute("books", recentFour );
        return "index";
    }
}
