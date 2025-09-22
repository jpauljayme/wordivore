package dev.jp.wordivore.controller;

import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.dto.WordOfTheDayDefinitions;
import dev.jp.wordivore.dto.WordOfTheDayExample;
import dev.jp.wordivore.dto.WordOfTheDayResponse;
import dev.jp.wordivore.model.LibrarySection;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.service.ShelfReadService;
import dev.jp.wordivore.service.WordnikService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final ShelfReadService shelfReadService;
    private final WordnikService wordnikService;

    @Value("${CLOUDFRONT_URL}")
    private String prefix;

    @GetMapping("/user")
    public String getUser(Model model, @AuthenticationPrincipal SecurityUser securityUser){

        model.addAttribute("username", securityUser.getUsername());
        model.addAttribute("prefix", prefix);

        List<LibrarySection> library = shelfReadService.getUserLibraryAllSections(securityUser.getUserId());

        List<LibraryItemDto> libraryToRead = library.getFirst().books();
        model.addAttribute("libraryToRead", libraryToRead);
        model.addAttribute("libraryToReadCount", libraryToRead.size());

        List<LibraryItemDto> libraryCurrentReads = library.get(1).books();
        model.addAttribute("libraryCurrentReads", libraryCurrentReads );
        model.addAttribute("libraryCurrentReadsCount", libraryCurrentReads.size());


        WordOfTheDayResponse wordOfTheDay = wordnikService.getWordOfTheDay(LocalDate.now());
        if(wordOfTheDay == null){
            WordOfTheDayResponse emptyWotd = new WordOfTheDayResponse("Hello",
                "Wordnik API is currently down.",
                LocalDate.now(),
                List.of(new WordOfTheDayDefinitions("No definition found",
                        "none", ""
                )),
                List.of(new WordOfTheDayExample(
                    "",
                    "No example found.",
                    ""
                ))
            );

            model.addAttribute("wotd", emptyWotd);
        }else{
            model.addAttribute("wotd", wordOfTheDay);
        }


        return "index";
    }
}
