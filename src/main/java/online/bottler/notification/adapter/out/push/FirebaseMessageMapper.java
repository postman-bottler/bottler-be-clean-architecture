package online.bottler.notification.adapter.out.push;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import online.bottler.notification.domain.PushMessage;
import online.bottler.notification.domain.PushMessages;

@Component
public class FirebaseMessageMapper {

    public List<Message> mapToFirebaseMessages(PushMessages messages) {
        List<Message> firebaseMessages = new ArrayList<>();
        for (PushMessage message : messages.getMessages()) {
            Message firebaseMessage = mapToFirebaseMessage(message);
            firebaseMessages.add(firebaseMessage);
        }
        return firebaseMessages;
    }

    private Message mapToFirebaseMessage(PushMessage message) {
        return Message.builder()
                .setToken(message.getToken())
                .setNotification(Notification.builder()
                        .setTitle(message.getTitle())
                        .setBody(message.getContent())
                        .build())
                .build();
    }
}
