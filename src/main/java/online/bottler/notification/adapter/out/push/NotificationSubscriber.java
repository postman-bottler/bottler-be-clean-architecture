package online.bottler.notification.adapter.out.push;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import online.bottler.global.exception.AdaptorException;
import online.bottler.notification.domain.PushMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
public class NotificationSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String userId;
    private final SseEmitter sseEmitter;

    public NotificationSubscriber(SseEmitter sseEmitter, String userId) {
        this.sseEmitter = sseEmitter;
        this.userId = userId;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PushMessage pushMessage = objectMapper.readValue(message.getBody(), PushMessage.class);
            sseEmitter.send(
                    SseEmitter.event()
                            .id(userId)
                            .name(pushMessage.getTitle())
                            .data(pushMessage.getContent())
            );
        } catch (IOException e) {
            log.error("SSE 알림 전송 중 오류 발생: {}", e.getMessage());
            throw new AdaptorException("SSE 알림 전송 실패하였습니다.");
        }
    }
}
