package secure_app.backend.security;

import jakarta.persistence.*;
import secure_app.backend.user.User;

import java.time.Instant;

@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_tokens_user_id", columnList = "user_id")
        }
)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private Instant expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public RefreshToken(){}

    public RefreshToken(
            String tokenHash,
            Instant expiresAt,
            User user
    ){
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public Long getId(){
        return id;
    }

    public Instant getExpiresAt(){
        return expiresAt;
    }

    public User getUser(){
        return user;
    }

    public void setTokenHash(String tokenHash){
        this.tokenHash = tokenHash;
    }

    public void setExpiresAt(Instant expiresAt){
        this.expiresAt = expiresAt;
    }

    public void setUser(User user){
        this.user = user;
    }


}
