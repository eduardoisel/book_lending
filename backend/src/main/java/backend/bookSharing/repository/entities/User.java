package backend.bookSharing.repository.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Entity()
@Table(name = "App_User")
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class User implements UserDetails {

    @Transient public static final int maxEmailSize = 70;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @Size(max = maxEmailSize, message = "{validation.name.size.too_long}") private String email;

    /**
     * Possibly to be changed so it is on another table
     */
    @JsonIgnore @Setter private Boolean locked = false;

    @Column(name = "has_admin_powers")
    private Boolean isAdmin;

    @JsonIgnore @ToString.Exclude private PasswordValidationInfo validationInfo;

    @JsonIgnore
    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;

    //    @JdbcTypeCode(SqlTypes.GEOGRAPHY)
    //    private Geometry<G2D> location;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY) // note: mapped by string value is from owned class user
    // reference name member
    @ToString.Exclude
    private List<Owned> owned;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            orphanRemoval =
                    true) // note: mapped by string value is from owned class user reference name
    // member
    @ToString.Exclude
    private List<Token> tokens;

    public User(Point point, String email, PasswordValidationInfo validationInfo) {
        this.location = point;
        this.email = email;
        this.validationInfo = validationInfo;
        this.isAdmin = false;
    }

    public User(Point point, String email, PasswordValidationInfo validationInfo, Boolean isAdmin) {
        this.location = point;
        this.email = email;
        this.validationInfo = validationInfo;
        this.isAdmin = isAdmin;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @JsonIgnore
    @Override
    public @Nullable String getPassword() {
        return validationInfo.hash();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
