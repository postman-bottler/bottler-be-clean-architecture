package online.bottler.notification.application.request;

public record NotificationLabelRequestDTO(
        Long letterId,
        String label
) {
}
