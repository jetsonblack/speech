package edu.iu.speech.bootstrap;

import org.springframework.context.annotation.Profile;
import edu.iu.speech.data.entities.*;
import edu.iu.speech.data.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Profile({ "dev", "prod" })
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
                if (speechRepository.count() > 0)
                        return;

                var p1 = personRepository.save(new Person("Jetson Black"));
                var p2 = personRepository.save(new Person("Mary Black"));
                var p3 = personRepository.save(new Person("Test"));

                var c1 = categoryRepository.save(new Category("Test"));
                var c2 = categoryRepository.save(new Category("Mom"));
                var c3 = categoryRepository.save(new Category("Jetson"));

                speechRepository.save(new Speech(
                                "I spent 8 hours on this",
                                "im sleepy i wanna go to bed :)",
                                "https://jetsonblack.com/pain.mp3",
                                p1, c3));

                speechRepository.save(new Speech(
                                "I can't believe this",
                                "Jetson is not very smart",
                                null,
                                p2, c2));

                speechRepository.save(new Speech(
                                "Testing Testing",
                                "So, first of all, I give up",
                                null,
                                p3, c1));
        }
}