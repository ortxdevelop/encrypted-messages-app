package secure_app.backend.auth;

import secure_app.backend.security.RefreshTokenService;
import secure_app.backend.security.RateLimitService;
import secure_app.backend.user.User;
import secure_app.backend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import secure_app.backend.exception.UsernameAlreadyExistsException;
import secure_app.backend.exception.InvalidCredentialsException;
import secure_app.backend.exception.RateLimitExceededException;
import secure_app.backend.security.JwtService;

@Service
public class AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final RateLimitService rateLimitService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            RateLimitService rateLimitService
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.rateLimitService = rateLimitService;
    }

    public String generateAccessToken(User user) {
        return jwtService.generateToken(user.getUsername());
    }

    public String generateRefreshToken(User user) {
        return refreshTokenService.create(user);
    }

    public User refreshUser(String refreshToken) {
        return refreshTokenService.validate(refreshToken);
    }

    public User login(String username, String password){
        String normalizedUsername = username.trim().toLowerCase();

        // Check rate limit before attempting login
        if (!rateLimitService.isAllowed(normalizedUsername)) {
            throw new RateLimitExceededException(
                    "Too many login attempts. Please try again later"
            );
        }

        User user = userRepository.findByUsername(normalizedUsername)
                .orElseThrow(()->{
                    rateLimitService.recordFailedAttempt(normalizedUsername);
                    return new InvalidCredentialsException("Invalid username or password");
                });

        if (!passwordEncoder.matches(password, user.getPassword())){
            rateLimitService.recordFailedAttempt(normalizedUsername);
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Reset counter on successful login
        rateLimitService.resetAttempts(normalizedUsername);
        return user;
    }


    public User register(String username, String password){
        username = username.trim().toLowerCase();

        if (userRepository.findByUsername(username).isPresent()){
            throw new UsernameAlreadyExistsException("Username is already exist");
        }

        User user = new User(
                username,
                passwordEncoder.encode(password)
        );

        return userRepository.save(user);
    }

    public void logout(String refreshToken){
        refreshTokenService.delete(refreshToken);
    }
}