package dev.jp.emancipate_the_self.controller;

import dev.jp.emancipate_the_self.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/user")
    public String getUser(Model model, Authentication a){
        model.addAttribute("username", a.getName());
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model, Authentication a){

        model.addAttribute("username", a.getName());
        return "index";
    }
}
