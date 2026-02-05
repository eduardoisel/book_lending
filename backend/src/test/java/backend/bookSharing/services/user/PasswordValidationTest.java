package backend.bookSharing.services.user;

import backend.bookSharing.RandomValuesGenerator;
import backend.bookSharing.services.user.services.PasswordValidation;
import static org.junit.jupiter.api.Assertions.*;

import backend.bookSharing.utils.PasswordValidationInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordValidationTest {

    //import org.junit.Test;

    @Autowired
    PasswordValidation passwordValidation = new PasswordValidation();

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

        assertThrowsExactly(
                PasswordValidation.NoSpecialCharException.class,
                ()-> passwordValidation.isSafePassword(password));


    }

    @Test
    public void fullWorkingPassword() {
        String password = "abcDEFGHIJKL_\"";

        int  a = '_';
        int b = '"';
        try {
            passwordValidation.isSafePassword(password);
        } catch (Exception e) {
            fail("Threw exception, should have passed", e);
        }
    }

    @Test
    public void encodingAndMatchingTest(){
        String password = RandomValuesGenerator.password();
        String salt = passwordValidation.getSalt();

        String encodedPassword = passwordValidation.passwordEncoding(password, salt);

        //assertEquals(passwordValidation.passwordEncoding(password), encodedPassword);

        assertTrue(passwordValidation.validatePassword(password, new PasswordValidationInfo(encodedPassword, salt)));
    }

}
