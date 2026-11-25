package backend.bookSharing.repository.entities;

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

/**
 * @implNote Since a user's role is none or being an admin, value indicating it is a boolean {@link User#isAdmin}
 */
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
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(unique = true, nullable = false)
    @Size(max = maxEmailSize, message = "{validation.name.size.too_long}") private String email;

    @Setter private Boolean locked = false;

    @Column(name = "has_admin_powers", nullable = false)
    private Boolean isAdmin;

    @ToString.Exclude private PasswordValidationInfo validationInfo;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;

    // note: mapped by string value is from owned class user reference name member
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Owned> owned;

    // note: mapped by string value is from owned class user reference name member
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @ToString.Exclude
    private List<Token> tokens;

    @OneToMany(mappedBy = "requester")
    @ToString.Exclude
    private List<Request> requests;

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

    /**
     * @return admin authority or none
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.isAdmin) {
            return List.of((GrantedAuthority) () -> "ROLE_ADMIN");
        }
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return validationInfo.hash();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
}
