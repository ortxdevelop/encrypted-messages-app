package secure_app.backend.message;

public class MessageResponse {

    private final Long id;
    private final String text;

    public MessageResponse(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId(){
        return id;
    }

    public String getText() {
        return text;
    }
}
