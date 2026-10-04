package secure_app.backend.auth;

import secure_app.backend.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.Map;
import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
    ) {

        User user = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        String accessToken = authService.generateAccessToken(user);
        String refreshToken = authService.generateRefreshToken(user);

        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("api/auth")
                .maxAge(Duration.ofDays(30))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Map.of(
                                "token", accessToken,
                                "username", user.getUsername()
                        )
                );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        User user = authService.register(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "username", user.getUsername()
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue("refresh_token") String refreshToken
    ) {
        User user = authService.refreshUser(refreshToken);

        String accessToken = authService.generateAccessToken(user);

        return ResponseEntity.ok(Map.of("token", accessToken)
        );
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(
            @CookieValue(
                    value="refresh_token",
                    required = false
            )String refreshToken
    ){
        if (refreshToken != null){
            authService.logout(refreshToken);
        }

        ResponseCookie deleteCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("api/auth")
                .maxAge(Duration.ofDays(30))
                .build();

        return ResponseEntity.noContent()
                .header(
                        HttpHeaders.SET_COOKIE,
                        deleteCookie.toString()
                )
                .build();
    }

}
