package dev.jp.emancipate_the_self.controller;

import ch.qos.logback.core.model.Model;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxReswap;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRetarget;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxSwapType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String landing(Model model){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        return "index";
    }
}
