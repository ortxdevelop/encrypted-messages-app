package secure_app.backend.security;

import org.springframework.stereotype.Service;
import secure_app.backend.user.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS =
            30L * 24 * 60 * 30;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();


    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository
    ){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String create(User user){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        String tokenHash = hash(token);

        RefreshToken refreshToken = new RefreshToken(
                tokenHash,
                Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS),
                user
        );

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    private String hash(String token){
        try{
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder()
                    .encodeToString(hash);

        }catch(NoSuchAlgorithmException e){
            throw new IllegalStateException(
                    "SHA-256 algorithm is not available", e
            );
        }
    }

    public User validate(String token){
        String tokenHash = hash(token);

        RefreshToken refreshToken = refreshTokenRepository.
                findByTokenHash(tokenHash)
                .orElseThrow(()->
                        new IllegalArgumentException("Invalid refresh token")
                );

        if (refreshToken.getExpiresAt().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);

            throw new IllegalArgumentException("Refresh token expired");

        }

        return refreshToken.getUser();
    }

    public void delete(String token){
        String tokenHash = hash(token);

        refreshTokenRepository.
                findByTokenHash(tokenHash)
                .ifPresent(refreshTokenRepository::delete);
    }
}
