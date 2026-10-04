package secure_app.backend.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class RegisterRequest {
    @NotBlank(message = "Username must not be blank")
    @Size(
            min = 3,
            max = 20,
            message = "Username must be between 3 and 20 characters"
    )
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username can contain only letters, numbers and underscore"
    )
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(
            min = 8,
            max = 64,
            message = "Password must between 8 and 64 characters"
    )
    private String password;

    public RegisterRequest(){}

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
