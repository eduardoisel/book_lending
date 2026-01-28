package backend.bookSharing.services.user.services;

import backend.bookSharing.utils.PasswordValidationInfo;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
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

    public String getSalt() {
        byte[] salt = new byte[2]; // TODO database needs to be at least size of array. Search for possible automation
        secureRandom.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }


    public Boolean validatePassword(
            String password,
            PasswordValidationInfo validationInfo
    ) {
        return passwordEncoder.matches(password + validationInfo.salt(), validationInfo.hash());
    }

    /**
     * Encodes password as a way to avoid saving raw passwords on server side.
     *
     * @param password raw password
     * @param salt
     * @return Hashed password
     */
    public String passwordEncoding(String password, String salt) {
        return passwordEncoder.encode(password + salt);
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

        if (!password.matches(".*[a-z].*")){
            throw new NoLowerCaseException();
        }

        if (!password.matches(".*[A-Z].*")){
            throw new NoUpperCaseException();
        }

        if (!password.matches(".*[!$%^&*()_+|~=`´{}:\";'\\\\<>?,./].*")){
            throw new NoSpecialCharException();
        }

    }

}
