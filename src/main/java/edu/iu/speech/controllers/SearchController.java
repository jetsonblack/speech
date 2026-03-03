package edu.iu.speech.controllers;

import edu.iu.speech.data.entities.Speech;
import edu.iu.speech.data.repositories.SpeechRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final SpeechRepository speechRepository;
    public SearchController(SpeechRepository speechRepository) {
        this.speechRepository = speechRepository;
    }

    @GetMapping
    public String searchPage(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "mode", required = false, defaultValue = "person") String mode,
            Model model
    ) {
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("mode", mode);

        // No query yet: just show the form
        if (q == null || q.trim().isEmpty()) {
            model.addAttribute("results", List.of());
            model.addAttribute("searched", false);
            return "search";
        }
        // we take results based on which mode we are in, we query depedning on mode
        List<Speech> results = switch (mode) {
            case "topic" -> speechRepository.searchByCategoryName(q.trim());
            case "title" -> speechRepository.searchByTitleOrText(q.trim());
            default -> speechRepository.searchByPersonName(q.trim());
        };
        model.addAttribute("results", results);
        model.addAttribute("searched", true);
        return "search";
    }
}