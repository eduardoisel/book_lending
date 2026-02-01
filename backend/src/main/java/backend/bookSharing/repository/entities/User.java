package backend.bookSharing.repository.entities;


import backend.bookSharing.utils.PasswordValidationInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.NoArgsConstructor;

@Getter
@Entity()
@Table(name = "App_User")
@EqualsAndHashCode
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    /**
     * hash(?) of clear password and salt
     */
    @JsonIgnore
    @Column(length = 256, nullable = false)
    private String hash;

    @JsonIgnore
    @Column(length = 2, nullable = false)
    private String salt;

    @ManyToOne
    @JoinColumn(name = "region") //assumes region_name without
    private Region region;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) //note: mapped by string value is from owned class user reference name member
    private List<Owned> owned;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true) //note: mapped by string value is from owned class user reference name member
    private List<Token> tokens;

    public User(Region region, String email, String hash, String salt) {
        this.region = region;
        this.email = email;
        this.hash = hash;
        this.salt = salt;
    }

    @JsonIgnore
    public PasswordValidationInfo getValidationInfo(){
        return new PasswordValidationInfo(hash, salt);
    }

    @Override
    public String toString() {
        return String.format("User[id='%d', email='%s']", id, email);
    }

}
