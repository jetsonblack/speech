package edu.iu.speech;
//    ___________________________________________________________________________
//                            <TimeTest.java>
//                    formated with formated with Checkstyle Extension for Java (VScode)
//                    adapted from textbook, taco application and previous
//					  Spring Projects.
//    ___________________________________________________________________________
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeTest  {

    @Autowired
    private MockMvc mockMvc;

    void checkResponseTime(String url) throws Exception {
        long start = System.currentTimeMillis();

        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        long duration = System.currentTimeMillis() - start;

        if (duration > 2000) {
            throw new AssertionError(
                    "this: " + url + "took too long (more than 2000ms)"
            );
        }
    }

    @Test
    void testRoutesResponse() throws Exception {
        checkResponseTime("/");
        checkResponseTime("/toc");
        checkResponseTime("/all");
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminRoutesSpeed() throws Exception {
        checkResponseTime("/admin");
    }
    
}
