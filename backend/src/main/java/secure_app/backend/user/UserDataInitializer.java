package secure_app.backend.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import secure_app.backend.user.User;
import secure_app.backend.user.UserRepository;

@Configuration
public class UserDataInitializer {

    @Bean
    CommandLineRunner initUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("test").isEmpty()) {
                User user = new User("test", passwordEncoder.encode("TestPassword1"));
                userRepository.save(user);
                System.out.println("Test user created: test / TestPassword1");
            }
        };
    }
}