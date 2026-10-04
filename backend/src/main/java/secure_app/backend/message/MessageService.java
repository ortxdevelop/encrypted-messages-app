package secure_app.backend.message;

import secure_app.backend.crypto.EncryptionService;
import secure_app.backend.user.User;
import secure_app.backend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    public MessageService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            EncryptionService encryptionService
    ){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    public Message create(String username, String text){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String encryptedText = encryptionService.encrypt(text);

        Message message = new Message(encryptedText, user);

        return messageRepository.save(message);
    }

    public Page<Message> getUserMessages(
            String username,
            Pageable pageable
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.findAllByUserOrderByIdDesc(
                user,
                pageable
        );
    }

    public String decrypt(Long messageId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Message message = messageRepository.findByIdAndUser(messageId, user)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        return encryptionService.decrypt(message.getEncryptedText());
    }
}
