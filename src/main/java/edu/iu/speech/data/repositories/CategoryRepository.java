package edu.iu.speech.data.repositories;

import edu.iu.speech.data.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}