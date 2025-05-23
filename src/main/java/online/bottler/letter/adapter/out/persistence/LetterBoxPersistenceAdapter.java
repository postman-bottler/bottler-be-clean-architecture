package online.bottler.letter.adapter.out.persistence;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.out.CreateLetterBoxPort;
import online.bottler.letter.domain.Letter;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterBoxPersistenceAdapter implements CreateLetterBoxPort {
    @Override
    public Letter saveLetter(Long userId, Long letterId, LocalDateTime createdAt) {
        return null;
    }

    @Override
    public Letter saveReplyLetter(Long userId, Long letterId, LocalDateTime createdAt) {
        return null;
    }

    @Override
    public Letter saveRecommendedLetter(Long userId, Long letterId, LocalDateTime createdAt) {
        return saveLetter(userId, letterId, createdAt);
    }
}
