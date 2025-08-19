package dev.jp.wordivore.controller;

import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.service.BookService;
import dev.jp.wordivore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BookService bookService;

    @GetMapping("/user")
    public String getUser(Model model, @AuthenticationPrincipal SecurityUser securityUser){
        model.addAttribute("username", securityUser.getUsername());
        model.addAttribute("books",  bookService.getUserLibrary(securityUser.getUserId()));
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal SecurityUser securityUser){

        model.addAttribute("username", securityUser.getUsername());
        model.addAttribute("books",  bookService.getUserLibrary(securityUser.getUserId()));
        return "index";
    }
}
