package edu.iu.speech.data.entities;
//    ___________________________________________________________________________
//                            <Speech.java>
//                    formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "speeches")
public class Speech {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

// use lob here for large/big ahh speeches, was using just normal @Column but found
// this worked better with huge lorem ipsum test, claude helped debug this one.
    @Lob 
    @Column(nullable = false)
    private String content;

    @Column(name = "audio_url")
    private String audioUrl;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    protected Speech() {}

    public Speech(String title, String content, String audioUrl, Person person, Category category) {
        this.title = title;
        this.content = content;
        this.audioUrl = audioUrl;
        this.person = person;
        this.category = category;
    }

    public Long getId() { 
        return id;
    }
    public String getTitle() { 
        return title; 
    }
    public String getContent() { 
        return content; 
    }
    public String getAudioUrl() { 
        return audioUrl; 
    }
    public Person getPerson() { 
        return person; 
    }
    public Category getCategory() { 
        return category;
    }

    public void setTitle(String title) { 
        this.title = title; 
    }
    public void setContent(String content) { 
        this.content = content; 
    }
    public void setAudioUrl(String audioUrl) { 
        this.audioUrl = audioUrl; 
    }
    public void setPerson(Person person) { 
        this.person = person; 
    }
    public void setCategory(Category category) { 
        this.category = category; 
    }
}