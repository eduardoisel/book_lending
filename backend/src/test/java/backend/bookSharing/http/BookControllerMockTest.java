package backend.bookSharing.http;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.bookSharing.http.controller.BookController;
import backend.bookSharing.services.book.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {BookController.class})
public class BookControllerMockTest extends ControllerMockTestBase {

    @Autowired private BookService bookService;

    // @MockitoBean private UserService userService;

    @Autowired private MockMvc mockMvc;

    @Test
    @WithMockUser
    void returnsBooksFromService() throws Exception {
        when(bookService.getBooks(0)).thenReturn(Page.empty());

        mockMvc.perform(get("/books/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.hasNextPage").value(false))
                .andExpect(jsonPath("$.hasPreviousPage").value(false));
    }
}
