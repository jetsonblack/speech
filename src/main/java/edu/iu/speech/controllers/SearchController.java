package edu.iu.speech.controllers;
//    ___________________________________________________________________________
//                            <SearchController.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.speech.data.entities.Speech;
import edu.iu.speech.data.repositories.SpeechRepository;
import edu.iu.speech.services.TocService;

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
            @RequestParam(name = "sort", required = false, defaultValue = "category") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir,
            Model model
    ) {
        // get query or null
        // set defaults for sorting
        String query = q == null ? "" : q.trim();
        String normalizedSort = normalizeSort(sort);
        String normalizedDir = normalizeDir(dir);
        String normalizedMode = normalizeMode(mode);

        model.addAttribute("sort", normalizedSort);
        model.addAttribute("dir", normalizedDir);
        model.addAttribute("tocGrouped", tocService.getTocGroupedByCategory(normalizedDir));
        model.addAttribute("tocList", tocService.getSortedSpeeches(normalizedSort, normalizedDir));
        model.addAttribute("q", query);
        model.addAttribute("mode", mode);

        // No query yet: just show the form
        if (query.isEmpty()) {
            model.addAttribute("results", List.of());
            model.addAttribute("searched", false);
            return "index";
        }
        // we take results based on which mode we are in, we query depedning on mode
        List<Speech> results = switch (normalizedMode) {
            case "topic" -> speechRepository.searchByCategoryName(query);
            case "title" -> speechRepository.searchByTitleOrText(query);
            default -> speechRepository.searchByPersonName(query);
        };

        model.addAttribute("results", results);
        model.addAttribute("searched", true);
        return "index";
    }

    // was having some weird behavior with sort, added this and it fixed it
    // basically handles nulls and switches depending on 
    private String normalizeSort(String sort) {
        if (sort == null) {
            return "category";
        }
        return switch (sort.toLowerCase()) {
            case "title", "author", "audio", "category" -> sort.toLowerCase();
            default -> "category";
        };
    }

    private String normalizeDir(String dir) {
        // default to ascending order
        if (dir == null) {
            return "asc";
        }
        
        return "desc".equalsIgnoreCase(dir) ? "desc" : "asc";
    }

    private String normalizeMode(String mode) {
        // default to person
        if (mode == null) {
            return "person";
        }

        return switch (mode.toLowerCase()) {
            case "person", "topic", "title" -> mode.toLowerCase();
            default -> "person";
        };
    }
    
}