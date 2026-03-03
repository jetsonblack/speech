package edu.iu.speech.controllers;

import edu.iu.speech.data.repositories.SpeechRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class SpeechController {
    private final SpeechRepository speechRepository;

    public SpeechController(SpeechRepository speechRepository) {
        this.speechRepository = speechRepository;
    }

    @GetMapping("/speech/{id}")
    public String speech(@PathVariable Long id, Model model) {
        var speech = speechRepository.findByIdWithRefs(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("speech", speech);
        return "speech";
    }
}