package backend.bookSharing.repository.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;

@Entity()
@Table(name = "App_User")
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Column(unique = true)
    private String email;

    @Getter
    @Column(length = 256)
    private String password;

    @Getter
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

    /**
     * @return Every owned book at once?
     */
    public List<Owned> getOwned(){
        return owned;
    }

}
