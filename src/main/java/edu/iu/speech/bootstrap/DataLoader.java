package edu.iu.speech.bootstrap;

import org.springframework.context.annotation.Profile;
import edu.iu.speech.data.entities.*;
import edu.iu.speech.data.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Profile({"dev","prod"})
@Component
public class DataLoader implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final CategoryRepository categoryRepository;
    private final SpeechRepository speechRepository;

    public DataLoader(PersonRepository personRepository,
                      CategoryRepository categoryRepository,
                      SpeechRepository speechRepository) {
        this.personRepository = personRepository;
        this.categoryRepository = categoryRepository;
        this.speechRepository = speechRepository;
    }

    @Override
    public void run(String... args) {
        if (speechRepository.count() > 0) return;

        var p1 = personRepository.save(new Person("Jetson Black"));
        var p2 = personRepository.save(new Person("Martin Luther King Jr."));
        var p3 = personRepository.save(new Person("Franklin D. Roosevelt"));

        var c1 = categoryRepository.save(new Category("Deperssion"));
        var c2 = categoryRepository.save(new Category("Civil Rights"));
        var c3 = categoryRepository.save(new Category("War"));

        speechRepository.save(new Speech(
                "I spent 8 hours on this",
                "im sleepy i wanna go to bed :)",
                null,
                p1, c1
        ));

        speechRepository.save(new Speech(
                "I Have a Dream",
                "I have a dream that one day ...",
                "https://jetsonblack.com/pain.mp3",
                p2, c2
        ));

        speechRepository.save(new Speech(
                "First Inaugural Address",
                "So, first of all, let me assert my firm belief ...",
                null,
                p3, c3
        ));
    }
}