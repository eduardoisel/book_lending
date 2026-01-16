package backend.bookSharing.services.user.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
/*
    todo change to use salt
 */
public class PasswordValidation {

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final SecureRandom secureRandom;

    {
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // todo use
    public byte[] getNextSalt() {
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }


    public Boolean validatePassword(
            String password,
            String validationInfo
    ) {
        return passwordEncoder.matches(password, validationInfo);
    }

    /**
     * Encodes password as a way to avoid saving raw passwords on server side.
     *
     * @param password raw password
     * @return Hashed password
     */
    public String passwordEncoding(String password) {
        return passwordEncoder.encode(password);
    }


    public static class InsufficientSizeException extends Exception{

    }

    public static class NoLowerCaseException extends Exception{

    }

    public static class NoUpperCaseException extends Exception{

    }

    public static class NoSpecialCharException extends Exception{

    }

    /**
     * Checks if password contains various symbols as a strength check
     *
     * @param password raw password
     *
     * @throws InsufficientSizeException if is too small
     *
     * @throws NoLowerCaseException if it has no lower case character
     *
     * @throws NoUpperCaseException if it has no upper case character
     *
     * @throws NoSpecialCharException if it has no special character
     *
     */
    public void isSafePassword(String password) throws InsufficientSizeException, NoLowerCaseException, NoUpperCaseException, NoSpecialCharException {

        if (password.length() < 8){
            throw new InsufficientSizeException();
        }

        if (!password.matches("[a-z]+")){
            throw new NoLowerCaseException();
        }

        if (!password.matches("[A-Z]+")){
            throw new NoUpperCaseException();
        }

        if (!password.matches("[!$%^&*()_+|~=`´{}:\";'\\<>?,./]+")){
            throw new NoSpecialCharException();
        }

    }

}
