package edu.iu.speech.controllers;
//    ___________________________________________________________________________
//                            <SpeechController.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import edu.iu.speech.data.repositories.SpeechRepository;
// https://www.baeldung.com/spring-pathvariable
// lets path be id
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