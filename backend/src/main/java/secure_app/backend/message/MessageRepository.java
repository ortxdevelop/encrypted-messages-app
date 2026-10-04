package secure_app.backend.message;

import secure_app.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByUserOrderByIdDesc(User user, Pageable pageable);
    Optional<Message> findByIdAndUser(Long id, User user);
}