package backend.bookSharing.http;

import backend.bookSharing.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class BookControllerTest extends ControllerTestBase {

    @Test
    public void addBookByIsbn10() throws Exception{

        String isbn10 = TestData.booksExclusiveFromApi[0].getIsbnTen();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/books/{isbn}", isbn10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void addBookByIsbn13() throws Exception{

        String isbn13 = TestData.booksExclusiveFromApi[0].getIsbnThirteen();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/books/{isbn}", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }


    @Test
    public void addBookTwice() throws Exception{

        String isbn10 = TestData.booksExclusiveFromApi[0].getIsbnTen();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/books/{isbn}", isbn10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/books/{isbn}", isbn10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

}
