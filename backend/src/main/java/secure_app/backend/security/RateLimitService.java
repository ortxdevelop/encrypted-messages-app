package secure_app.backend.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory rate limiting for login attempts.
 * Blocks users after 5 failed attempts for 15 minutes.
 */
@Service
public class RateLimitService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MS = 15 * 60 * 1000;

    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public boolean isAllowed(String username) {
        LoginAttempt attempt = attempts.get(username);

        if (attempt == null) {
            return true;
        }

        // Reset if lockout period expired
        if (System.currentTimeMillis() - attempt.getLastAttemptTime() > LOCKOUT_DURATION_MS) {
            attempts.remove(username);
            return true;
        }

        return attempt.getAttemptCount() < MAX_ATTEMPTS;
    }

    public void recordFailedAttempt(String username) {
        LoginAttempt attempt = attempts.getOrDefault(
                username,
                new LoginAttempt()
        );

        attempt.incrementAttempts();
        attempts.put(username, attempt);
    }

    public void resetAttempts(String username) {
        attempts.remove(username);
    }

    private static class LoginAttempt {
        private int attemptCount = 0;
        private long lastAttemptTime = System.currentTimeMillis();

        public void incrementAttempts() {
            this.attemptCount++;
            this.lastAttemptTime = System.currentTimeMillis();
        }

        public int getAttemptCount() {
            return attemptCount;
        }

        public long getLastAttemptTime() {
            return lastAttemptTime;
        }
    }
}
