package backend.bookSharing.repository.entities;

import jakarta.persistence.*;

@Entity()
@Table(name = "App_User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

    @ManyToOne
    private Region region;

    public User(Region region, String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

}
