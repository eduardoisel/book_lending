package backend.bookSharing.http;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.bookSharing.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

// todo check why it is not recognized
@WebMvcTest()
public class SpringDocTest extends ControllerMockTestBase {

    @Autowired private MockMvc mockMvc;

    @Test
    void isAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/swagger-ui.html").with(user(TestData.users.getFirst())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/swagger-ui/index.html#/")).andExpect(status().isOk());
        mockMvc.perform(get("/v3/api-docs.yaml")).andExpect(status().isOk());
    }
}
