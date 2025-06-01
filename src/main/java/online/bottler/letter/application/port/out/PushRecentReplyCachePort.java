package online.bottler.letter.application.port.out;

public interface PushRecentReplyCachePort {
    void push(Long id, String label, Long receiverId);
}
