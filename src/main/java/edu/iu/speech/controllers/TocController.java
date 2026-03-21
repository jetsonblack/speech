package edu.iu.speech.controllers;

import edu.iu.speech.services.TocService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;


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