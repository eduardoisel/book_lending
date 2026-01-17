package backend.bookSharing.services.user;

import backend.bookSharing.services.user.services.PasswordValidation;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
//@ContextConfiguration(classes = PasswordValidation.class)
//@ComponentScan("backend.bookSharing.services.user.services")
public class PasswordValidationTest {

    //import org.junit.Test;

    @Autowired
    PasswordValidation passwordValidation;// = new PasswordValidation();

    @Test
    public void failureBySize() {

        String password = "AbC_";

        try {
            passwordValidation.isSafePassword(password);
            fail("Should have thrown exception");
        } catch (PasswordValidation.InsufficientSizeException _) {

        } catch (Exception e) {
            fail(e);
        }

    }

    @Test
    public void failureMissingLowerCase() {

        String password = "ABCDEFGHI_";

        try {
            passwordValidation.isSafePassword(password);
            fail("Should have thrown exception");
        } catch (PasswordValidation.NoLowerCaseException _) {

        } catch (Exception e) {
            fail(e);
        }

    }

    @Test
    public void failureMissingUpperCase() {

        String password = "abcdefghij_";

        try {
            passwordValidation.isSafePassword(password);
            fail("Should have thrown exception");
        } catch (PasswordValidation.NoUpperCaseException _) {

        } catch (Exception e) {
            fail(e);
        }

    }

    @Test
    public void failureMissingSpecialChar() {
        String password = "abcDEFGHIJKL";

        try {
            passwordValidation.isSafePassword(password);
            fail("Should have thrown exception");
        } catch (PasswordValidation.NoSpecialCharException _) {

        } catch (Exception e) {
            fail(e);
        }

    }

    @Test
    public void fullWorkingPassword() {
        String password = "abcDEFGHIJKL_\"";

        try {
            passwordValidation.isSafePassword(password);
        } catch (Exception e) {
            fail("Threw exception, should have passed");
        }
    }

}
