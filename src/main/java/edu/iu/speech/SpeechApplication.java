package edu.iu.speech;

//    ___________________________________________________________________________
//                            <SpeechApplication.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeechApplication {

  // OK did some testing, the reason for build errors on windows computer when
  // compared to mac/unix based system
  // like render is a difference in definition of the data folder
  public static void main(String[] args) {
    SpringApplication.run(SpeechApplication.class, args);
  }

}