package backend.bookSharing.http;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.bookSharing.TestData;
import backend.bookSharing.http.controller.UserController;
import backend.bookSharing.http.data.IsbnString;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

// @ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(controllers = {UserController.class})
public class UserControllerMockTest extends ControllerMockTestBase {

    @Autowired private UserService userService;

    @Autowired private MockMvc mockMvc;

    @Autowired private UserController userController;

    @Test
    public void userControllerValidationTest() throws Exception {

        User user = TestData.users.getFirst();

        when(userService.checkAuthentication(any())).thenReturn(user);

        ResultActions postActions =
                mockMvc.perform(
                        post("/users/owned/{ISBN}", "abcdef123")
                                .with(user(TestData.users.getFirst())));

        postActions.andExpect(status().isUnprocessableContent());

        assertThrowsExactly(
                ConstraintViolationException.class,
                () -> userController.addBookOwned(new IsbnString("abcdefg"), null));
    }

    @Test
    @WithMockUser
    void returnsOwnedBooksFromService() throws Exception {
        when(userService.getOwnedBooks(42, 0)).thenReturn(Page.empty());

        mockMvc.perform(get("/users/ownedBy/{userId}", 42))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.hasNextPage").value(false))
                .andExpect(jsonPath("$.hasPreviousPage").value(false));
    }
}
