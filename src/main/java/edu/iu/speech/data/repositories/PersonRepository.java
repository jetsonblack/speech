package edu.iu.speech.data.repositories;

import edu.iu.speech.data.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}