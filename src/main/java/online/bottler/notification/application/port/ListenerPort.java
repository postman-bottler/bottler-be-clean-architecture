package online.bottler.notification.application.port;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ListenerPort {
    void save(String id, SseEmitter sseEmitter);

    void delete(String id);
}
