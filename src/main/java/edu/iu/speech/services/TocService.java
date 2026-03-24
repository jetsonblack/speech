package edu.iu.speech.services;
//    ___________________________________________________________________________
//                            <TocService.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.iu.speech.data.entities.Speech;
import edu.iu.speech.data.repositories.SpeechRepository;


// https://howtodoinjava.com/java/collections/java-comparator/
// comparator seems like the best way to normalize and compare sort logic
// use later for personal website

@Service
public class TocService {
    private final SpeechRepository speechRepository;

    public TocService(SpeechRepository speechRepository) {
        this.speechRepository = speechRepository;
    }

    public Map<String, List<Speech>> getTocGroupedByCategory(String dir) {
        List<Speech> speeches = new ArrayList<>(speechRepository.findAllForToc());
        Comparator<Speech> comparator = Comparator
                .comparing((Speech s) -> safeString(s.getCategory().getName()), String.CASE_INSENSITIVE_ORDER)
                .thenComparing(s -> safeString(s.getTitle()), String.CASE_INSENSITIVE_ORDER);

        if ("desc".equalsIgnoreCase(dir)) {
            comparator = comparator.reversed();
        }
        speeches.sort(comparator);
        // get all speeches here
        Map<String, List<Speech>> grouped = new LinkedHashMap<>();
        for (Speech speech : speeches) {
            String categoryName = speech.getCategory().getName();
            grouped.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(speech);
        }
        return grouped;
    }
    public List<Speech> getSortedSpeeches(String sort, String dir) {
        // ok, we use switch depending on sort mode, 
        // then for each sort mode we will use comparator to sort between the speeches
        // depending on the type of sorting, used claude to help debug this since i don't 
        // really know how to use comparator, 
        List<Speech> speeches = new ArrayList<>(speechRepository.findAllForToc());
        Comparator<Speech> comparator = switch (sort) {
            // sort via title
            case "title" -> Comparator
                    .comparing((Speech s) -> safeString(s.getTitle()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getPerson().getName()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getCategory().getName()), String.CASE_INSENSITIVE_ORDER);
            // sort bia author
            case "author" -> Comparator
                    .comparing((Speech s) -> safeString(s.getPerson().getName()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getTitle()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getCategory().getName()), String.CASE_INSENSITIVE_ORDER);
            // sort via audio eval
            case "audio" -> Comparator
                    .comparing((Speech s) -> hasAudio(s) ? 1 : 0)
                    .thenComparing(s -> safeString(s.getTitle()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getPerson().getName()), String.CASE_INSENSITIVE_ORDER);
            // sort via category
            case "category" -> Comparator
                    .comparing((Speech s) -> safeString(s.getCategory().getName()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getTitle()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getPerson().getName()), String.CASE_INSENSITIVE_ORDER);
            // default to category
            default -> Comparator
                    .comparing((Speech s) -> safeString(s.getCategory().getName()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getTitle()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(s -> safeString(s.getPerson().getName()), String.CASE_INSENSITIVE_ORDER);
        };
        // if descending, reverse the collection, then put the collection into speech and return
        if ("desc".equalsIgnoreCase(dir)) {
            comparator = comparator.reversed();
        }
        speeches.sort(comparator);
        return speeches;
    }
    private boolean hasAudio(Speech speech) { return speech.getAudioUrl() != null && !speech.getAudioUrl().isBlank(); }
    private String safeString(String value) { return value == null ? "" : value; }
}