package backend.bookSharing.services.user.services;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidation {

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public Boolean validatePassword(
            String password,
            String validationInfo
            ) {
        return passwordEncoder.matches(password, validationInfo);
    }

    /**
     * Encodes password as a way to avoid saving raw passwords on server side.
     * @param password raw password
     * @return Hashed password
     */
    public String passwordEncoding(String password){
        return passwordEncoder.encode(password);
    }

    // todo really stress test regex
    /**
     * Checks if password contains various symbols as a strength check
     * @param password raw password
     * @return True if passes the tests, false otherwise
     */
    public Boolean isSafePassword(String password) {

        //&& password.matches("[!$%^&*()_+|~=`´{}:\";'<>?,./\\]+");
        return password.length() > 8 //minimum size
                && password.matches("[a-z]+")
                && password.matches("[A-Z]+")
                && password.matches("[!$%^&*()_+|~=`´{}:\";'\\<>?,./]+");
                }

}
