package edu.iu.speech.data.repositories;
//    ___________________________________________________________________________
//                            <CategoryRepository.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import org.springframework.data.jpa.repository.JpaRepository;

import edu.iu.speech.data.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}