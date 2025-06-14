package online.bottler.notification.adapter.out.persistence;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import online.bottler.notification.domain.Device;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmbeddedDevice {

    private String token;

    public EmbeddedDevice(Device device) {
        this.token = device.getToken();
    }

    public Device toDomain() {
        return new Device(token);
    }
}
