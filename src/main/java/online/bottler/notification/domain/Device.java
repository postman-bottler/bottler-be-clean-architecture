package online.bottler.notification.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Device {
    private final String token;

    public Device(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(token, device.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }
}
