package backend.bookSharing.http;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.bookSharing.http.controller.BookController;
import backend.bookSharing.http.data.LendCreation;
import backend.bookSharing.http.data.RequestCreation;
import backend.bookSharing.services.book.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {BookController.class})
public class BookControllerMockTest extends ControllerMockTestBase {

    @Autowired private BookService bookService;

    // @MockitoBean private UserService userService;

    @Autowired private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getBookOwnersValidationTest() throws Exception {
        mockMvc.perform(get("/books/owners/{ISBN}", "abcdefg"))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @WithMockUser
    void requestBookValidationTest() throws Exception {

        RequestCreation body = new RequestCreation("abcdefg", null, 0);
        mockMvc.perform(
                        post("/books/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(body)))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @WithMockUser
    void lendBookValidationTest() throws Exception {

        LendCreation body = new LendCreation("abcdefg", null);
        mockMvc.perform(
                        post("/books/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(body)))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void returnsBooksFromService() throws Exception {
        when(bookService.getBooks(0)).thenReturn(Page.empty());

        mockMvc.perform(get("/books/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.hasNextPage").value(false))
                .andExpect(jsonPath("$.hasPreviousPage").value(false));
    }
}
