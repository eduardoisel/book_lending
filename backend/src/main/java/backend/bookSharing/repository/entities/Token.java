package backend.bookSharing.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SourceType;

/**
 * not necessary if we limit amount of sessions for an account to one. Then delete table and change values to user
 * <p>
 * Authentication tokens for users
 */
@Getter
@NoArgsConstructor
@ToString(doNotUseGetters = true)
@Entity()
@Table(name = "Token")
@DynamicInsert
public class Token {

    @Id
    @Column(name = "token_validation", length = 256)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Timestamp createdDate;

    @Setter
    @Column(name = "last_used_at", nullable = false, insertable = false, updatable = true)
    @CurrentTimestamp(source = SourceType.DB)
    private Timestamp lastUsed;

    /**
     * @param token authentication token
     * @param user user to who belongs the token
     */
    public Token(String token, User user) {
        this.token = token;
        this.user = user;
//        this.lastUsed = Timestamp.from(Instant.now());
//        this.createdDate = Timestamp.from(Instant.now());
    }

}
