package online.bottler.notification.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PushMessage {
    private Long userId;

    private Device device;

    private String title;

    private String content;

    public String getDeviceToken() {
        return device.getToken();
    }
}
