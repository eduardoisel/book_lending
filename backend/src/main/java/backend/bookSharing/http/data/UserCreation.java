package backend.bookSharing.http.data;

/**
 * data class with information necessary to create user
 * @param email email to be associated to user, cannot be in use for another account at the same time
 * @param password clear password to use on login
 * @param x x coordinate of user location
 * @param y y coordinate of user location
 */
public record UserCreation(String email, String password, double x, double y){

}
