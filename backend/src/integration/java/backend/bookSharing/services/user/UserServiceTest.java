package backend.bookSharing.services.user;

import static org.junit.jupiter.api.Assertions.*;

import backend.bookSharing.RandomValuesGenerator;
import backend.bookSharing.TestData;
import backend.bookSharing.repository.entities.Book;
import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.ServiceTestBase;
import backend.bookSharing.services.user.failures.LogoutError;
import backend.bookSharing.services.user.failures.UserAuthenticationError;
import backend.bookSharing.services.user.failures.UserCreationError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

// @TestConfiguration(proxyBeanMethods = false)
public class UserServiceTest extends ServiceTestBase {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void successfulUserCreation() {

        try {
            userService.createUser(
                    RandomValuesGenerator.email(),
                    RandomValuesGenerator.password(),
                    RandomValuesGenerator.randomBetween(-180, 180),
                    RandomValuesGenerator.randomBetween(-90, 90));
        } catch (Exception e) {
            fail("User creation should be successful", e);
        }
    }

    @Test
    public void userCreationWithWeakPassword() {

        String validUniqueEmail = RandomValuesGenerator.email();
        String invalidPassword = "weak";

        int validLongitude = RandomValuesGenerator.randomBetween(-180, 180);
        int validLatitude = RandomValuesGenerator.randomBetween(-90, 90);

        assertThrowsExactly(
                UserCreationError.WeakPassword.class,
                () ->
                        userService.createUser(
                                validUniqueEmail, invalidPassword, validLongitude, validLatitude));
    }

    @Test
    public void userCreationWithNonUniqueEmail() {

        String repeatedEmail = TestData.users.getFirst().getEmail();
        String validPassword = RandomValuesGenerator.password();

        int validLongitude = RandomValuesGenerator.randomBetween(-180, 180);
        int validLatitude = RandomValuesGenerator.randomBetween(-90, 90);

        assertThrowsExactly(
                UserCreationError.EmailInUse.class,
                () ->
                        userService.createUser(
                                repeatedEmail, validPassword, validLongitude, validLatitude));
    }

    @Test
    public void successfulLogin() {
        try {
            userService.login(
                    TestData.clearPasswordUsers[0].email(),
                    TestData.clearPasswordUsers[0].clearPassword());
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
        String token = "";
        try {
            token =
                    userService.login(
                            TestData.clearPasswordUsers[0].email(),
                            TestData.clearPasswordUsers[0].clearPassword());
        } catch (Exception _) {
            fail("User login should be successful");
        }

        try {
            userService.logout(token);
        } catch (Exception _) {
            fail("User login should be successful");
        }
    }

    @Test
    public void addOwnerOfBook() throws Exception {

        User owner = insertedUsers.getFirst(); // does not have id

        Book book = TestData.databaseBooks[0];

        userService.addOwner(book.getIsbnTen(), owner);
    }
}
