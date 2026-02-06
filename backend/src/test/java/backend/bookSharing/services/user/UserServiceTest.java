package backend.bookSharing.services.user;

import backend.bookSharing.RandomValuesGenerator;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.services.ServiceTestBase;


import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import static org.junit.jupiter.api.Assertions.*;

//@TestConfiguration(proxyBeanMethods = false)
public class UserServiceTest extends ServiceTestBase {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }


    @Test
    public void successfulUserCreation() {

        try {
            userService.createUser(RandomValuesGenerator.email(), RandomValuesGenerator.password());
        } catch (Exception _) {
            fail("User creation should be successful");
        }

    }

    @Test
    public void userCreationWithWeakPassword() {

        String validUniqueEmail = RandomValuesGenerator.email();
        String invalidPassword = "weak";

        assertThrowsExactly(
                UserCreationError.WeakPassword.class,
                () -> userService.createUser(validUniqueEmail, invalidPassword));

    }

    @Test
    public void userCreationWithNonUniqueEmail() {

        String repeatedEmail = TestData.users.getFirst().getEmail();
        String validPassword = RandomValuesGenerator.password();

        assertThrowsExactly(
                UserCreationError.EmailInUse.class,
                () -> userService.createUser(repeatedEmail, validPassword));

    }

    @Test
    public void successfulLogin() {
        try {
            userService.login(TestData.clearPasswordUsers[0].email(), TestData.clearPasswordUsers[0].clearPassword());
        } catch (Exception _) {
            fail("User login should be successful");
        }
    }

    @Test
    public void unsuccessfulLogin() {

        String invalidEmail = TestData.users.getFirst().getEmail();
        String validPassword = RandomValuesGenerator.password();

        assertThrowsExactly(
                UserAuthenticationError.UserOrPasswordAreInvalid.class,
                () -> userService.login(invalidEmail, validPassword));

    }

    @Test
    public void unsuccessfulLogout() {

        String bogusToken = "good morning usa";

        assertThrowsExactly(
                LogoutError.TokenInvalidForAuthentication.class,
                () -> userService.logout(bogusToken));

    }

    @Test
    public void successfulLogout() {
        //login user
        String token = "";
        try {
            token = userService.login(TestData.clearPasswordUsers[0].email(), TestData.clearPasswordUsers[0].clearPassword());
        } catch (Exception _) {
            fail("User login should be successful");
        }

        try {
            userService.logout(token);
        } catch (Exception _) {
            fail("User login should be successful");
        }

    }

    /*
    For now fails due to not placing default values on timestamps
     */
    @Test
    public void addOwnerOfBook() throws Exception {

        TestData.ClearPasswordUsers owner = TestData.clearPasswordUsers[0]; // does not have id

        Book book = TestData.databaseBooks[0];

        String token = userService.login(owner.email(), owner.clearPassword());

        userService.addOwner(book.getIsbnTen(), token);


    }

}
