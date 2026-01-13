package backend.bookSharing.http.data;

public class UserCreation {

    public String email;

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
