package edu.iu.speech.data.entities;
import jakarta.persistence.*;

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