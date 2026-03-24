package edu.iu.speech.controllers;
//    ___________________________________________________________________________
//                            <TocController.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.speech.services.TocService;


@Controller
public class TocController {
    private final TocService tocService;

    public TocController(TocService tocService) {
        this.tocService = tocService;
    }

    @GetMapping({"/", "/tableofcontents", "/toc", "/all"})
    public String toc(
            @RequestParam(name = "sort", required = false, defaultValue = "category") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir,
            Model model
    ) {
        String normalizedSort = normalizeSort(sort);
        String normalizedDir = normalizeDir(dir);
        model.addAttribute("sort", normalizedSort);
        model.addAttribute("dir", normalizedDir);
        model.addAttribute("tocGrouped", tocService.getTocGroupedByCategory(normalizedDir));
        model.addAttribute("tocList", tocService.getSortedSpeeches(normalizedSort, normalizedDir));
        model.addAttribute("q", "");
        model.addAttribute("mode", "person");
        model.addAttribute("results", List.of());
        model.addAttribute("searched", false);
        return "index";
    }



// might be redundent, but oh well
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
        if (dir == null) {
            return "asc";
        }

        return "desc".equalsIgnoreCase(dir) ? "desc" : "asc";
    }
}