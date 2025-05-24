package online.bottler.letter.v2.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.infra.LetterBoxJpaRepository;
import online.bottler.letter.infra.LetterBoxQueryRepository;
import online.bottler.letter.infra.entity.LetterBoxEntity;
import online.bottler.letter.v2.application.port.out.CheckLetterBoxPersistencePort;
import online.bottler.letter.v2.application.port.out.CreateLetterBoxPersistencePort;
import online.bottler.letter.v2.application.port.out.DeleteLetterBoxPersistencePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterBoxPersistenceAdapter implements CreateLetterBoxPersistencePort, CheckLetterBoxPersistencePort,
        DeleteLetterBoxPersistencePort {

    private final LetterBoxJpaRepository letterBoxJpaRepository;
    private final LetterBoxQueryRepository letterBoxQueryRepository;

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
        return letterBoxJpaRepository.existsByLetterIdAndUserId(letterId, userId);
    }

    @Override
    public void delete(Long letterId, LetterType letterType, BoxType boxType) {
        List<Long> letterIds = new ArrayList<>();
        letterIds.add(letterId);
        letterBoxQueryRepository.deleteByCondition(letterIds, letterType, boxType);
    }
}
