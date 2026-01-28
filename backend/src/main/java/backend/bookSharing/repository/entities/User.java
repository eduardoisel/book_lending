package backend.bookSharing.repository.entities;


import backend.bookSharing.utils.PasswordValidationInfo;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Entity()
@Table(name = "App_User")
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    /**
     * hash(?) of clear password and salt
     */
    @Column(length = 256, nullable = false)
    private String hash;

    @Column(length = 2, nullable = false)
    private String salt;

    @ManyToOne
    @JoinColumn(name = "region") //assumes region_name without
    private Region region;

    @OneToMany(mappedBy = "user") //note: mapped by string value is from owned class user reference name member
    private List<Owned> owned;

    @OneToMany(mappedBy = "user", orphanRemoval = true) //note: mapped by string value is from owned class user reference name member
    private List<Token> tokens;

    public User(){} //seems to be necessary for hibernate

    public User(Region region, String email, String hash, String salt) {
        this.region = region;
        this.email = email;
        this.hash = hash;
        this.salt = salt;
    }

    public PasswordValidationInfo getValidationInfo(){
        return new PasswordValidationInfo(hash, salt);
    }

    @Override
    public String toString() {
        return String.format("User[id='%d', email='%s']", id, email);
    }

}
