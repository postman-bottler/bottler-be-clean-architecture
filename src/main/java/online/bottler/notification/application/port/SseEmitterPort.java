package online.bottler.notification.application.port;

import java.util.Optional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterPort {

    void save(String id, SseEmitter sseEmitter);

    Optional<SseEmitter> findById(String id);

    void delete(String id);
}
