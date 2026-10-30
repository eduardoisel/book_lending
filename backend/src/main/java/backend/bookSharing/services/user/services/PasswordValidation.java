package backend.bookSharing.services.user.services;

import backend.bookSharing.repository.entities.PasswordValidationInfo;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidation {

    private final PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final SecureRandom secureRandom;

    {
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSalt() {
        byte[] salt = new byte[PasswordValidationInfo.saltSize];
        secureRandom.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    public Boolean validatePassword(String password, PasswordValidationInfo validationInfo) {
        return passwordEncoder.matches(password + validationInfo.salt(), validationInfo.hash());
    }

    /**
     * Encodes password as a way to avoid saving raw passwords on server side.
     *
     * @param password raw password
     * @param salt random salt
     * @return Hashed password
     */
    public String passwordEncoding(String password, String salt) {
        return passwordEncoder.encode(password + salt);
    }
}
