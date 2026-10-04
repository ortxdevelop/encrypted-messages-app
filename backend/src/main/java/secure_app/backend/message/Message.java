package secure_app.backend.message;

import secure_app.backend.user.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "messages",
        indexes = {
                @Index(name = "idx_messages_user_id", columnList = "user_id")
        }
)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String encryptedText;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Message(){}

    public Message(String encryptedText, User user){
        this.encryptedText = encryptedText;
        this.user = user;
    }

    public Long getId(){
        return id;
    }

    public String getEncryptedText(){
        return encryptedText;
    }

    public User getUser(){
        return user;
    }

    public void setEncryptedText(String text){
        this.encryptedText = text;
    }

    public void setUser(User user){
        this.user = user;
    }
}
