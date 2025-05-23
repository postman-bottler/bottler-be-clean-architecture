package online.bottler.letter.adapter.out.persistence;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterBoxEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterBoxJpaRepository;
import online.bottler.letter.application.port.out.CheckLetterBoxPort;
import online.bottler.letter.application.port.out.CreateLetterBoxPort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterBoxPersistenceAdapter implements CreateLetterBoxPort, CheckLetterBoxPort {

    private final LetterBoxJpaRepository letterBoxJpaRepository;

    @Override
    public void createForLetter(Long letterId, Long userId, LocalDateTime createdAt) {
        save(letterId, userId, LetterType.LETTER, BoxType.SEND, createdAt);
    }

    @Override
    public void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt) {
        save(letterId, userId, LetterType.REPLY_LETTER, BoxType.SEND, createdAt);
        save(letterId, receiverId, LetterType.REPLY_LETTER, BoxType.RECEIVE, createdAt);
    }

    @Override
    public void createForRecommendedLetter(Long letterId, Long userId, LocalDateTime createdAt) {
        createForLetter(letterId, userId, createdAt);
    }

    private void save(Long letterId, Long userId, LetterType letterType, BoxType boxType, LocalDateTime createdAt) {
        letterBoxJpaRepository.save(
                LetterBoxEntity.from(LetterBox.create(letterId, userId, letterType, boxType, createdAt))).toDomain();
    }

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return false;
    }
}
