package secure_app.backend.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MessageRequest {

    @NotBlank(message = "Message cannot be empty")
    @Size(
            max = 5000,
            message = "Message cannot exceed 5000 characters"
    )
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}