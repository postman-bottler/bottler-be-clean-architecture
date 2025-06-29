package online.bottler.notification.adapter.out.push;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import online.bottler.notification.application.port.SseEmitterPort;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseEmitterAdapter implements SseEmitterPort {

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    public void save(String id, SseEmitter sseEmitter) {
        sseEmitters.put(id, sseEmitter);
    }

    @Override
    public Optional<SseEmitter> findById(String id) {
        return Optional.ofNullable(sseEmitters.get(id));
    }

    @Override
    public void delete(String id) {
        sseEmitters.remove(id);
    }
}
