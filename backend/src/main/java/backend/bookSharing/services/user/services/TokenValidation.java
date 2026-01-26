package backend.bookSharing.services.user.services;

import backend.bookSharing.repository.entities.Token;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class TokenValidation {
    //private Integer maxNumberOfTokensPerUser = config.maxTokensPerUser;

    public static class TokenValidTime{
        private final Duration tokenTtl;

        private final Duration tokenTtlRolling;

        /**
         * @param tokenTtl Total time the token can be valid since creation. Must be positive
         * @param tokenTtlRolling Total time the token can be valid since it was last used. Must be positive
         *
         * @throws  InvalidParameterException if any are negative
         */
        public TokenValidTime(Duration tokenTtl, Duration tokenTtlRolling){

            if (tokenTtl.isNegative()){
                throw new InvalidParameterException("Token time to live cannot be negative");
            }

            if (tokenTtlRolling.isNegative()){
                throw new InvalidParameterException("Token rolling time to live cannot be negative");
            }

            this.tokenTtl = tokenTtl;
            this.tokenTtlRolling = tokenTtlRolling;
        }
    }
    private final Duration tokenTtl;

    private final Duration tokenTtlRolling;

    private static final Integer tokenSizeInBytes = 256; //token given to user

    public TokenValidation(TokenValidTime tokenValidTime) {

        this.tokenTtl = tokenValidTime.tokenTtl;
        this.tokenTtlRolling = tokenValidTime.tokenTtlRolling;

    }

    //not created in methods to ensure anything that may cause exception to happen as quickly as possible.
    // In the end should be substituted by unit tests.

    private final MessageDigest messageDigest;

    {
        try {
            messageDigest = MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private final SecureRandom secureRandom;

    {
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String hash(String input) {
        return Base64.getUrlEncoder().encodeToString(
                messageDigest.digest(input.getBytes())
        );
    }

    public String generateTokenValue() {

        byte[] array = new byte[tokenSizeInBytes];

        secureRandom.nextBytes(array);

        return Base64.getUrlEncoder().encodeToString(array);

    }


    public Boolean canBeToken(String token) {
        return Base64.getUrlDecoder().decode(token).length == tokenSizeInBytes;
    }

    /**
     * Checks if token has expired
     *
     * @param now   Input instant of when it expired, placed for ease of testing
     * @param token token information
     * @return True if yet to expire.
     */
    public Boolean hasTokenExpired(Instant now, Token token) {
        //Duration d = Duration.ofHours(6);d.minus(1, TemporalUnit)
        //Period p = Period.ZERO.withDays(1);

        now.minus(tokenTtl);

        boolean basicCheck = token.getCreatedDate().toInstant().isAfter(now);
        boolean ttl = token.getCreatedDate().toInstant().plus(tokenTtl).isAfter(now);
        boolean tokenTtlBool = token.getLastUsed().toInstant().plus(tokenTtlRolling).isAfter(now);

        return token.getCreatedDate().toInstant().isAfter(now)
                && token.getCreatedDate().toInstant().plus(tokenTtl).isAfter(now)
                && token.getLastUsed().toInstant().plus(tokenTtlRolling).isAfter(now);
    }


    public String createTokenValidationInformation(String token) {
        return hash(token);
    }

}
