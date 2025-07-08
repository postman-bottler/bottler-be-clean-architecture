package online.bottler.letter.application.port.out;

public interface PushRecentReplyCachePort {
    void push(Long receiverId, Long id, String label);
}
