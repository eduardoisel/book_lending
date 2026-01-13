package backend.bookSharing.repository.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity()
@Table(name = "App_User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "region") //assumes region_name without
    private Region region;

    @OneToMany(mappedBy = "user") //note: mapped by string value is from owned class user reference name member
    private List<Owned> owned;

    @OneToMany(mappedBy = "user", orphanRemoval = true) //note: mapped by string value is from owned class user reference name member
    private List<Token> tokens;

    public User(){} //seems to be necessary for hibernate

    public User(Region region, String email, String password) {
        this.region = region;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User[id='%d', email='%s']", id, email);
    }

    public String getEmail() {
        return email;
    }

    /**
     * @return Every owned book at once?
     */
    public List<Owned> getOwned(){
        return owned;
    }

    /**
     * @return Every owned book at once?
     */
    public Integer getId(){
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Region getRegion() {
        return region;
    }
}
