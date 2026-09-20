package backend.bookSharing.http.data;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * data class with information necessary to create user
 *
 * @param email    email to be associated to user, cannot be in use for another account at the same time
 * @param password clear password to use on login
 * @param x        x coordinate of user location
 * @param y        y coordinate of user location
 */
public record UserCreation(
        @JsonSetter(nulls = Nulls.FAIL) String email,
        @JsonSetter(nulls = Nulls.FAIL) String password,
        double x,
        double y) {}
