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
    private final TocService tocService;

    public SearchController(SpeechRepository speechRepository, TocService tocService) {
        this.speechRepository = speechRepository;
        this.tocService = tocService;
    }

    @GetMapping
    public String searchPage(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "mode", required = false, defaultValue = "person") String mode,
            Model model
    ) {
        // get query or null
        String query = q == null ? "" : q.trim();

        model.addAttribute("toc", tocService.getTocGroupedByCategory());
        model.addAttribute("q", query);
        model.addAttribute("mode", mode);

        // No query yet: just show the form
        if (query.isEmpty()) {
            model.addAttribute("results", List.of());
            model.addAttribute("searched", false);
            return "index";
        }
        // we take results based on which mode we are in, we query depedning on mode
        List<Speech> results = switch (mode) {
            case "topic" -> speechRepository.searchByCategoryName(query);
            case "title" -> speechRepository.searchByTitleOrText(query);
            default -> speechRepository.searchByPersonName(query);
        };

        model.addAttribute("results", results);
        model.addAttribute("searched", true);
        return "index";
    }
}