package online.bottler.notification.application.port;

import online.bottler.notification.domain.PushMessages;

public interface PushNotificationPort {
    public void pushAll(PushMessages pushMessages);
}
