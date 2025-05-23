package online.bottler.letter.application.port.out;

import java.time.LocalDateTime;

public interface CreateLetterBoxPort {
    void saveLetter(Long userId, Long letterId, LocalDateTime createdAt);
    void saveReplyLetter(Long userId, Long receiverId, Long letterId, LocalDateTime createdAt);
    void saveRecommendedLetter(Long userId, Long letterId, LocalDateTime createdAt);
}
