package edu.iu.speech;
//    ___________________________________________________________________________
//                            <AdminControllerTest.java>
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest  {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAdminRequiresAuth() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminAccess() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdminForbiddenForUser() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }
}