package online.bottler.letter.application.port.out;

public interface DeleteRecentReplyCachePort {
    void delete(Long receiverId, Long id, String label);
}
