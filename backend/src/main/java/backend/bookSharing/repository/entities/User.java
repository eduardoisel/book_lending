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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TODO see {@link org.springframework.security.core.userdetails.User} and related classes
 */
@Getter
@Entity()
@Table(name = "App_User")
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class User {

    @Transient
    public static final int saltSize = 2;
    @Transient
    public static final int maxEmailSize = 70;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @Size(max = maxEmailSize, message = "{validation.name.size.too_long}")
    private String email;

    /**
     * hash(?) of clear password and salt
     */
    @JsonIgnore
    @Column(length = 256, nullable = false)
    @ToString.Exclude
    private String hash;

    @JsonIgnore
    @Column(length = saltSize, nullable = false)
    @ToString.Exclude
    private String salt;

    @ManyToOne
    @JoinColumn(name = "region") //assumes region_name without
    private Region region;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) //note: mapped by string value is from owned class user reference name member
    @ToString.Exclude
    private List<Owned> owned;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true) //note: mapped by string value is from owned class user reference name member
    @ToString.Exclude
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

}
