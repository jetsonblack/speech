package edu.iu.speech.data.entities;
//    ___________________________________________________________________________
//                            <Person.java>
//                    formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    protected Person() {}
    public Person(String name) { 
        this.name = name; 
    }

    public Long getId() { 
        return id; 
    }
    public String getName() { 
        return name;
    }
    public void setName(String name) { 
        this.name = name; 
    }
}