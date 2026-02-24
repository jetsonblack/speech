package edu.iu.speech.services;

import edu.iu.speech.data.entities.Speech;
import edu.iu.speech.data.repositories.SpeechRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TocService {
    private final SpeechRepository speechRepository;

    public TocService(SpeechRepository speechRepository) {
        this.speechRepository = speechRepository;
    }

    public Map<String, List<Speech>> getTocGroupedByCategory() {
        List<Speech> speeches = speechRepository.findAllForToc();
        // get all speeches here

        // for each s in speeches we want to get their category and their name,
        Map<String, List<Speech>> grouped = new LinkedHashMap<>();
        for (Speech s : speeches) {
            String categoryName = s.getCategory().getName();
            grouped.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(s);
        }
        return grouped;
    }
}