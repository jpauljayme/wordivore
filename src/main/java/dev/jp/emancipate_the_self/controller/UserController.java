package dev.jp.emancipate_the_self.controller;

import dev.jp.emancipate_the_self.model.SecurityUser;
import dev.jp.emancipate_the_self.service.BookService;
import dev.jp.emancipate_the_self.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
