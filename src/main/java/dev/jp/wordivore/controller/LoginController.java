package dev.jp.wordivore.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class LoginController {

    @GetMapping("/")
    public String landing(Model model){
        return "index";
    }

    @GetMapping("/login")
    public String login( @RequestHeader(name = "HX-Request", required = false) String hx){
//        if(hx != null){
//            return "fragments/login :: login";
//        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        return "index";
    }
}
