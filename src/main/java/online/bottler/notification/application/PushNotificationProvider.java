package online.bottler.notification.application;

import online.bottler.notification.domain.PushMessages;

public interface PushNotificationProvider {
    public void pushAll(PushMessages pushMessages);
}
