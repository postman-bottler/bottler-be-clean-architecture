package online.bottler.notification.application;

import online.bottler.notification.domain.PushMessages;

public interface PushNotificationPort {
    public void pushAll(PushMessages pushMessages);
}
