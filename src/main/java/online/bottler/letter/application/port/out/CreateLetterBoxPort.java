package online.bottler.letter.application.port.out;

import java.time.LocalDateTime;

public interface CreateLetterBoxPort {
    void createForLetter(Long letterId, Long userId, LocalDateTime createdAt);
    void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt);
    void createForRecommendedLetter(Long letterId, Long userId, LocalDateTime createdAt);
}
