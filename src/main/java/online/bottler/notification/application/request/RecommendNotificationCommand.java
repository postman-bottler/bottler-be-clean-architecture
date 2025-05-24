package online.bottler.notification.application.request;

public record RecommendNotificationCommand(
        Long userId,
        Long letterId,
        String label
) {
    public static RecommendNotificationCommand of(Long userId, Long letterId, String label) {
        return new RecommendNotificationCommand(userId, letterId, label);
    }
}
