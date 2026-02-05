package backend.bookSharing;

import backend.bookSharing.repository.entities.User;
import backend.bookSharing.services.user.services.PasswordValidation;
import java.util.Random;


/**
 * Class generates random values to substitute the ones who would be inserted on the database
 * <p>
 * These methods are NOT responsible to ensure the values are unique in the database
 * <p>
 * <a href="https://www.baeldung.com/java-random-string">Used as reference</a>
 */
public class RandomValuesGenerator {

    static Random random = new Random();


    static int ZERO_CHAR = '0'; //48
    static int NINE_CHAR = '9'; //57
    static int UPPERCASE_A = 'A'; //65
    static int UPPERCASE_Z = 'Z'; //90
    static int LOWERCASE_A = 'a'; //97
    static int LOWERCASE_Z = 'z'; //122

    private static int randomBetween(int lowerBound, int higherBound) {
        if (lowerBound > higherBound) {
            int temp = lowerBound;
            lowerBound = higherBound;
            higherBound = lowerBound;
        }

        return lowerBound + (int) (random.nextFloat() * (higherBound - lowerBound + 1));

    }

    public static String generateNumeric(Integer size) {
        return random.ints(ZERO_CHAR, NINE_CHAR + 1)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateLowercase(Integer size) {
        return random.ints(LOWERCASE_A, LOWERCASE_Z + 1)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateUppercase(Integer size) {
        return random.ints(UPPERCASE_A, LOWERCASE_Z + 1)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateSpecialChar(Integer size) {
        return random.ints(0, Short.MAX_VALUE)
                .limit(size)
                .filter(i ->
                        !((i >= ZERO_CHAR && i <= NINE_CHAR)
                                || (i >= LOWERCASE_A && i <= LOWERCASE_Z)
                                || ((i >= UPPERCASE_A & i <= UPPERCASE_Z)))
                )
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();


    }

    /**
     * Only requirement is size as of now
     *
     * @return Acceptable email for database
     */
    public static String email() {
        return generateAlphaNumeric(randomBetween(15, User.maxEmailSize - 10)) + "@gmail.com";
    }

    /**
     * Requirements followed: size; lowercase and uppercase letters; numeric chars
     * @return Acceptable password for {@link PasswordValidation}
     */
    public static String password(){
        int requiredNumeric, requiredLowercase, requiredUppercase, requiredSpecial, requiredSize = 8;
        requiredNumeric =  requiredLowercase = requiredUppercase = requiredSpecial = 1;

        //not yet decided type of char
        int nonFixed  = requiredSize - (requiredLowercase + requiredNumeric + requiredUppercase + requiredSpecial);
        int numericExtra = randomBetween(requiredNumeric, nonFixed); nonFixed -= numericExtra;
        int lowercaseExtra = randomBetween(requiredLowercase, nonFixed); nonFixed -= lowercaseExtra;
        int uppercaseExtra = randomBetween(requiredUppercase, nonFixed); nonFixed -= uppercaseExtra;

        StringBuilder password = new StringBuilder(8);

        password.append(generateNumeric(numericExtra + 1));
        password.append(generateLowercase(lowercaseExtra + 1) );
        password.append(generateUppercase(uppercaseExtra + 1));
        password.append(generateSpecialChar(nonFixed + 1));

        return password.toString();
    }

    /**
     * @param size length of string
     * @return String, does NOT guarantee the existence of number or any other symbol
     */
    public static String generateAlphaNumeric(Integer size) {

        return random.ints(ZERO_CHAR, LOWERCASE_Z + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // not in  gap of numeric-Uppercase or gap Uppercase-Lowercase
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


}
