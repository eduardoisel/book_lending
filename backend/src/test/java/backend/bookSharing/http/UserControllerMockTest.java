package backend.bookSharing.http;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import backend.bookSharing.TestData;
import backend.bookSharing.http.controller.UserController;
import backend.bookSharing.http.data.IsbnString;
import backend.bookSharing.repository.entities.Owned;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.UserService;
import jakarta.validation.ConstraintViolationException;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public void bookRequestsOfUserValidationTest() throws Exception {

        User authUser = TestData.users.getFirst();

        when(userService.checkAuthentication(anyString())).thenReturn(authUser);

        ResultActions postActions =
                mockMvc.perform(post("/users/owned/{ISBN}", "abcdef123").with(user(authUser)));

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

    @Test
    void booksOwnedByUserUnauthenticatedSuccess() throws Exception {
        when(userService.getOwnedBooks(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/users/ownedBy/{userId}", 42)).andExpect(status().isOk());
    }

    @Test
    void bookRequestsSpecificUserAndBookUnauthenticatedSuccess() throws Exception {
        when(userService.getRequestsOfBook(any(), any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/users/ownedBy/{userId}/requests/{bookId}", 1, 1))
                .andExpect(status().isOk());
    }

    @Test
    void addBookAsOwnedUnauthenticatedFailure() throws Exception {
        mockMvc.perform(post("/users/owned/{ISBN}", 1)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void addBookAsOwnedAuthenticatedSuccess() throws Exception {

        when(userService.addOwner(any(), any())).thenReturn(new Owned());

        String isbn = TestData.booksExclusiveFromApi[0].getIsbnTen();
        Objects.requireNonNull(isbn);

        mockMvc.perform(post("/users/owned/{ISBN}", isbn)).andExpect(status().isOk());
    }

    @Test
    void blacklistEmailUnauthenticatedFailure() throws Exception {
        mockMvc.perform(post("/users/blacklist/{email}", "email"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * fails on filter due to null header, but unlike {@link #blacklistEmailUnauthenticatedFailure()},
     * one that also does not have header and also lacks {@link WithMockUser},
     * will have status {@link HttpStatus#FORBIDDEN}
     */
    @Test
    @WithMockUser
    void addBookAsOwnedAuthenticatedWithoutRoleFailure() throws Exception {
        mockMvc.perform(post("/users/blacklist/{email}", 1)).andExpect(status().isForbidden());
    }

    @Test
    void addBookAsOwnedAuthenticatedWithRoleSuccess() throws Exception {
        doNothing().when(userService).lockAccount(anyString());

        User adminUser = TestData.users.getFirst(); //get(1)
        assertTrue(adminUser.getIsAdmin());

        when(userService.checkAuthentication(anyString())).thenReturn(adminUser);

        mockMvc.perform(
                        post("/users/blacklist/{email}", "email")
                                //.header("Authorization", "Bearer not null")
                                .with(user(adminUser))
                )
                .andExpect(status().isOk());
    }
}
