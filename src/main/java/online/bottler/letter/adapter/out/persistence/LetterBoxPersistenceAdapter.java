package online.bottler.letter.adapter.out.persistence;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterBoxEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterBoxJpaRepository;
import online.bottler.letter.application.port.out.CreateLetterBoxPort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterBoxPersistenceAdapter implements CreateLetterBoxPort {

    private final LetterBoxJpaRepository letterBoxJpaRepository;

    @Override
    public void saveLetter(Long userId, Long letterId, LocalDateTime createdAt) {
        save(userId, letterId, LetterType.LETTER, BoxType.SEND, createdAt);
    }

    @Override
    public void saveReplyLetter(Long userId, Long receiverId, Long letterId, LocalDateTime createdAt) {
        save(userId, letterId, LetterType.REPLY_LETTER, BoxType.SEND, createdAt);
        save(receiverId, letterId, LetterType.REPLY_LETTER, BoxType.RECEIVE, createdAt);
    }

    @Override
    public void saveRecommendedLetter(Long userId, Long letterId, LocalDateTime createdAt) {
        saveLetter(userId, letterId, createdAt);
    }

    private void save(Long userId, Long letterId, LetterType letterType, BoxType boxType, LocalDateTime createdAt) {
        letterBoxJpaRepository.save(
                LetterBoxEntity.from(LetterBox.create(userId, letterId, letterType, boxType, createdAt))).toDomain();
    }
}
