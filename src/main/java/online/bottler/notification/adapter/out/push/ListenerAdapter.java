package online.bottler.notification.adapter.out.push;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import online.bottler.notification.application.port.ListenerPort;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class ListenerAdapter implements ListenerPort {

    private final Map<String, MessageListener> listeners = new ConcurrentHashMap<>();
    private final RedisMessageListenerContainer container;

    public ListenerAdapter(RedisMessageListenerContainer container) {
        this.container = container;
    }

    @Override
    public void save(String id, SseEmitter sseEmitter) {
        NotificationSubscriber listener = new NotificationSubscriber(sseEmitter, id);
        listeners.put(id, listener);
        container.addMessageListener(listener, new ChannelTopic(id));
    }

    @Override
    public void delete(String id) {
        MessageListener listener = listeners.remove(id);
        if (listener != null) {
            container.removeMessageListener(listener, new ChannelTopic(id));
        }
    }
}
