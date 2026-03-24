package edu.iu.speech;
//    ___________________________________________________________________________
//                            <SearchControllerTest.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSearchRequiresAuth() throws Exception {
        mockMvc.perform(get("/search?q=test"))
                .andExpect(status().is3xxRedirection()); // redirected to login
    }
}