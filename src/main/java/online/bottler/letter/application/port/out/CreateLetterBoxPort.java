package online.bottler.letter.application.port.out;

import java.time.LocalDateTime;
import online.bottler.letter.domain.Letter;

public interface CreateLetterBoxPort {
    Letter saveLetter(Long userId, Long letterId, LocalDateTime createdAt);
    Letter saveReplyLetter(Long userId, Long letterId, LocalDateTime createdAt);
    Letter saveRecommendedLetter(Long userId, Long letterId, LocalDateTime createdAt);
}
