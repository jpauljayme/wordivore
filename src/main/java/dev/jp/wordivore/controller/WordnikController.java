package dev.jp.wordivore.controller;

import dev.jp.wordivore.service.WordnikService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/wordnik")
@RequiredArgsConstructor
public class WordnikController {

    private final WordnikService wordnikService;

    @GetMapping("/wotd")
    public String getWordOfTheDay(Model model){
        wordnikService.getWordOfTheDay(LocalDate.now());
        return "fragments/wordink :: wordOfTheDay";
    }
}
