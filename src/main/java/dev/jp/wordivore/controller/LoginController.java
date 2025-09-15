package dev.jp.wordivore.controller;

import ch.qos.logback.core.model.Model;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRedirect;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class LoginController {

    @GetMapping("/")
    public String landing(Model model){
        return "index";
    }

    @GetMapping("/login")
    public String login( @RequestHeader(name = "HX-Request", required = false) String hx){
        return "login";
    }

    @PostMapping("/logout")
    @HxRedirect(value = "/")
    public String logout(Model model){
        return "redirect:/";
    }
}
