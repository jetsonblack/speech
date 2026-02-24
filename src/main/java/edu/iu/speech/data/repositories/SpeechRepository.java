package edu.iu.speech.data.repositories;

import edu.iu.speech.data.entities.Speech;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface SpeechRepository extends JpaRepository<Speech, Long> {

    @Query("""
        select s
        from Speech s
        join fetch s.category c
        join fetch s.person p
        order by c.name asc, s.title asc
    """)
    List<Speech> findAllForToc();

    @Query("""
        select s
        from Speech s
        join fetch s.category
        join fetch s.person
        where s.id = :id
    """)
    Optional<Speech> findByIdWithRefs(@Param("id") Long id);
}