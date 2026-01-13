package backend.bookSharing.services.user.services;

import backend.bookSharing.repository.entities.Token;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

public class TokenValidation {
    //private Integer maxNumberOfTokensPerUser = config.maxTokensPerUser;

    private final Duration tokenTtl;

    private final Duration tokenTtlRolling;

    private final Integer tokenSizeInBytes = 256; //todo not make it hard coded

    /**
     * @param tokenTtl Total time the token can be valid since creation
     * @param tokenTtlRolling Total time the token can be valid since it was last used
     */
    public TokenValidation(Duration tokenTtl, Duration tokenTtlRolling) {
        this.tokenTtl = tokenTtl;
        this.tokenTtlRolling = tokenTtlRolling;
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

        return token.getCreatedDate().toInstant().isAfter(now)
                && token.getCreatedDate().toInstant().minus(tokenTtl).isAfter(now)
                && token.getLastUsed().toInstant().minus(tokenTtlRolling).isAfter(now);
    }


    public String createTokenValidationInformation(String token) {
        return hash(token);
    }

}
