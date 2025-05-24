package online.bottler.notification.adapter.out.push;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import online.bottler.notification.application.PushNotificationPort;
import online.bottler.notification.domain.PushMessages;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebasePushAdapter implements PushNotificationPort {
    private final FirebaseMessageMapper messageMapper;

    @Override
    @Async
    public void pushAll(PushMessages pushMessages) {
        List<Message> firebaseMessages = messageMapper.mapToFirebaseMessages(pushMessages);
        try {
            FirebaseMessaging.getInstance().sendEach(firebaseMessages);
        } catch (FirebaseMessagingException e) {
            log.error("알림 푸시 실패 : {}", e.getMessage());
        }
    }
}
