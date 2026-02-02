package backend.bookSharing.http.data;

/**
 * data class with information necessary to create user
 */
public class UserCreation {

    /**
     * email to be associated to user, cannot be in use for another account at the same time
     */
    public String email;

    /**
     * clear password to use on login
     */
    public String password;

    public UserCreation(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User[email='%s', password='%s']", email, password);
    }

}
