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
        loadSpeechFormData(model);
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

        String cleanTitle = normalizeRequired(title);
        String cleanContent = normalizeRequired(content);
        String cleanAudioUrl = normalizeBlank(audioUrl);

        if (cleanTitle == null || cleanContent == null || personOpt.isEmpty() || categoryOpt.isEmpty()) {
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

        loadSpeechFormData(model);
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

        String cleanTitle = normalizeRequired(title);
        String cleanContent = normalizeRequired(content);
        String cleanAudioUrl = normalizeBlank(audioUrl);

        if (speechOpt.isEmpty() || cleanTitle == null || cleanContent == null
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

    @GetMapping("/categories")
    public String categoryList(Model model) {
        List<Category> categories = categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    @GetMapping("/categories/new")
    public String newCategoryForm(Model model) {
        model.addAttribute("pageTitle", "New Category");
        model.addAttribute("formAction", "/admin/categories");
        model.addAttribute("category", null);
        return "admin/category-form";
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam String name) {
        String cleanName = normalizeRequired(name);
        if (cleanName == null) {
            return "redirect:/admin/categories";
        }

        Category category = new Category(cleanName);
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/{id}/edit")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            return "redirect:/admin/categories";
        }

        model.addAttribute("pageTitle", "Edit Category");
        model.addAttribute("formAction", "/admin/categories/" + id);
        model.addAttribute("category", categoryOpt.get());
        return "admin/category-form";
    }

    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id, @RequestParam String name) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        String cleanName = normalizeRequired(name);

        if (categoryOpt.isEmpty() || cleanName == null) {
            return "redirect:/admin/categories";
        }

        Category category = categoryOpt.get();
        category.setName(cleanName);
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        if (speechRepository.existsByCategoryId(id)) {
            return "redirect:/admin/categories";
        }

        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/people")
    public String personList(Model model) {
        List<Person> people = personRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        model.addAttribute("people", people);
        return "admin/people";
    }

    @GetMapping("/people/new")
    public String newPersonForm(Model model) {
        model.addAttribute("pageTitle", "New Person");
        model.addAttribute("formAction", "/admin/people");
        model.addAttribute("person", null);
        return "admin/person-form";
    }

    @PostMapping("/people")
    public String createPerson(@RequestParam String name) {
        String cleanName = normalizeRequired(name);
        if (cleanName == null) {
            return "redirect:/admin/people";
        }

        Person person = new Person(cleanName);
        personRepository.save(person);
        return "redirect:/admin/people";
    }

    @GetMapping("/people/{id}/edit")
    public String editPersonForm(@PathVariable Long id, Model model) {
        Optional<Person> personOpt = personRepository.findById(id);
        if (personOpt.isEmpty()) {
            return "redirect:/admin/people";
        }

        model.addAttribute("pageTitle", "Edit Person");
        model.addAttribute("formAction", "/admin/people/" + id);
        model.addAttribute("person", personOpt.get());
        return "admin/person-form";
    }

    @PostMapping("/people/{id}")
    public String updatePerson(@PathVariable Long id, @RequestParam String name) {
        Optional<Person> personOpt = personRepository.findById(id);
        String cleanName = normalizeRequired(name);

        if (personOpt.isEmpty() || cleanName == null) {
            return "redirect:/admin/people";
        }

        Person person = personOpt.get();
        person.setName(cleanName);
        personRepository.save(person);
        return "redirect:/admin/people";
    }

    @PostMapping("/people/{id}/delete")
    public String deletePerson(@PathVariable Long id) {
        if (speechRepository.existsByPersonId(id)) {
            return "redirect:/admin/people";
        }

        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
        }
        return "redirect:/admin/people";
    }

    private void loadSpeechFormData(Model model) {
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

    private String normalizeRequired(String value) {
        String trimmed = normalizeBlank(value);
        return trimmed == null ? null : trimmed;
    }
}