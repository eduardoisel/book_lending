package backend.bookSharing.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import backend.bookSharing.TestData;
import backend.bookSharing.http.returns.ListedData;
import backend.bookSharing.repository.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

public class UserControllerTest extends ControllerTestBase {

    @Autowired private ObjectMapper objectMapper;

    @WithMockUser
    @Test
    void addBookAsOwnedTest() throws Exception {
        String isbn = TestData.databaseBooks[0].getIsbnThirteen();

        mockMvc.perform(MockMvcRequestBuilders.post("/users/owned/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    void searchOwnedBooksOfUser() throws Exception {

        User owner = insertedUsers.getFirst();

        String returned =
                mockMvc.perform(MockMvcRequestBuilders.get("/users/owned/{userId}", owner.getId()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        ListedData listedData = objectMapper.readValue(returned, ListedData.class);

        assertEquals(
                insertedOwned.stream()
                        .filter(o -> o.getUser().getId().equals(owner.getId()))
                        .count(),
                listedData.data().length);
    }
}
