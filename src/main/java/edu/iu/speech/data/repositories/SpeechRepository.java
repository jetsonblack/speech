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

    @Query("""
            select s
            from Speech s
            left join s.person p
            left join s.category c
            where lower(p.person) like lower(concat('%', :q, '%'))
            order by s.title asc
    """)
    List<Speech> searchByPersonName(@Param("q") String q);

    @Query("""
        select s
        from Speech s
        left join s.person p
        left join s.category c
        where lower(c.name) like lower(concat('%', :q, '%'))
        order by s.title asc
    """)
    List<Speech> searchByCategoryName(@Param("q") String q);
    
    @Query("""
        select s from Speech s
        where lower(s.title) like lower(concat('%', :q, '%'))
        or s.content like concat('%', :q, '%')
        order by s.title asc
    """)
    List<Speech> searchByTitleOrText(@Param("q") String q);


}