package edu.iu.speech.controllers;

import edu.iu.speech.data.entities.Category;
import edu.iu.speech.data.entities.Person;
import edu.iu.speech.data.entities.Speech;
import edu.iu.speech.data.repositories.CategoryRepository;
import edu.iu.speech.data.repositories.PersonRepository;
import edu.iu.speech.data.repositories.SpeechRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final SpeechRepository speechRepository;
    private final PersonRepository personRepository;
    private final CategoryRepository categoryRepository;
     public AdminController(
            SpeechRepository speechRepository,
            PersonRepository personRepository,
            CategoryRepository categoryRepository
    ) {
        this.speechRepository = speechRepository;
        this.personRepository = personRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String adminHome() {
        return "admin/index";
    }
    @GetMapping("/speeches")
    public String speechList(Model model) {
        model.addAttribute("speeches", speechRepository.findAllForToc());
        return "admin/speeches";
    }
    @GetMapping("/speeches/new")
    public String newSpeechForm(Model model) {
        loadFormData(model);
        model.addAttribute("pageTitle", "New Speech");
        model.addAttribute("formAction", "/admin/speeches");
        model.addAttribute("speech", null);
        return "admin/speech-form";
    }
    @PostMapping("/speeches")
    public String createSpeech(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) String audioUrl,
            @RequestParam Long personId,
            @RequestParam Long categoryId
    ) {
        Optional<Person> personOpt = personRepository.findById(personId);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        String cleanTitle = title == null ? "" : title.trim();
        String cleanContent = content == null ? "" : content.trim();
        String cleanAudioUrl = normalizeBlank(audioUrl);

        if (cleanTitle.isEmpty() || cleanContent.isEmpty() || personOpt.isEmpty() || categoryOpt.isEmpty()) {
            return "redirect:/admin/speeches";
        }

        Speech speech = new Speech(
                cleanTitle,
                cleanContent,
                cleanAudioUrl,
                personOpt.get(),
                categoryOpt.get()
        );

        speechRepository.save(speech);
        return "redirect:/admin/speeches";
    }
    @GetMapping("/speeches/{id}/edit")
    public String editSpeechForm(@PathVariable Long id, Model model) {
        Optional<Speech> speechOpt = speechRepository.findByIdWithRefs(id);
        if (speechOpt.isEmpty()) {
            return "redirect:/admin/speeches";
        }

        loadFormData(model);
        model.addAttribute("pageTitle", "Edit Speech");
        model.addAttribute("formAction", "/admin/speeches/" + id);
        model.addAttribute("speech", speechOpt.get());
        return "admin/speech-form";
    }
    
    @PostMapping("/speeches/{id}")
    public String updateSpeech(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) String audioUrl,
            @RequestParam Long personId,
            @RequestParam Long categoryId
    ) {
        Optional<Speech> speechOpt = speechRepository.findById(id);
        Optional<Person> personOpt = personRepository.findById(personId);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        String cleanTitle = title == null ? "" : title.trim();
        String cleanContent = content == null ? "" : content.trim();
        String cleanAudioUrl = normalizeBlank(audioUrl);

        if (speechOpt.isEmpty() || cleanTitle.isEmpty() || cleanContent.isEmpty()
                || personOpt.isEmpty() || categoryOpt.isEmpty()) {
            return "redirect:/admin/speeches";
        }

        Speech speech = speechOpt.get();
        speech.setTitle(cleanTitle);
        speech.setContent(cleanContent);
        speech.setAudioUrl(cleanAudioUrl);
        speech.setPerson(personOpt.get());
        speech.setCategory(categoryOpt.get());

        speechRepository.save(speech);
        return "redirect:/admin/speeches";
    }

    @PostMapping("/speeches/{id}/delete")
    public String deleteSpeech(@PathVariable Long id) {
        if (speechRepository.existsById(id)) {
            speechRepository.deleteById(id);
        }
        return "redirect:/admin/speeches";
    }

    private void loadFormData(Model model) {
        List<Person> people = personRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        List<Category> categories = categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        model.addAttribute("people", people);
        model.addAttribute("categories", categories);
    }

    private String normalizeBlank(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}