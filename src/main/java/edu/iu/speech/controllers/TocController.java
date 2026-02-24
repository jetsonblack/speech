package edu.iu.speech.controllers;

import edu.iu.speech.services.TocService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TocController {
    private final TocService tocService;

    public TocController(TocService tocService) {
        this.tocService = tocService;
    }

    @GetMapping({"/tableofcontents", "/toc", "/all"})
    public String toc(Model model) {
        model.addAttribute("toc", tocService.getTocGroupedByCategory());
        return "toc";
    }
}