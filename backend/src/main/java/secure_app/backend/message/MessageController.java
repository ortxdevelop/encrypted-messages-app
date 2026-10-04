package secure_app.backend.message;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponse create(
            @Valid @RequestBody MessageRequest request,
            Authentication authentication
    ) {
        Message message = messageService.create(
                authentication.getName(),
                request.getText()
        );

        return new MessageResponse(
                message.getId(),
                message.getEncryptedText()
        );
    }

    @PostMapping("/{id}/decrypt")
    public MessageResponse decrypt(
            @PathVariable Long id,
            Authentication authentication
    ){
        String decryptedText = messageService.decrypt(
                id,
                authentication.getName()
        );

        return new MessageResponse(
                id,
                decryptedText
        );
    }

    @GetMapping
    public Page<MessageResponse> getMessages(
            Authentication authentication,
            Pageable pageable
    ) {

        return messageService
                .getUserMessages(
                        authentication.getName(),
                        pageable
                )
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getEncryptedText()
                ));
    }
}
