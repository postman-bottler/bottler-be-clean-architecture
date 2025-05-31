package online.bottler.letter.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterBoxEntity;
import online.bottler.letter.adapter.out.persistence.model.LetterSummaryProjection;
import online.bottler.letter.adapter.out.persistence.repository.LetterBoxJpaRepository;
import online.bottler.letter.adapter.out.persistence.repository.LetterBoxQueryRepository;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterBoxPersistenceAdapter implements LetterBoxPersistencePort {

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
    public void createForRecommendedLetter(Long letterId, Long userId) {
        save(letterId, userId, LetterType.LETTER, BoxType.SEND, LocalDateTime.now());
    }

    @Override
    public void createForDeveloperLetter(Long letterId, Long userId, LocalDateTime createdAt) {
        save(letterId, userId, LetterType.LETTER, BoxType.RECEIVE, LocalDateTime.now());
    }

    @Override
    public List<LetterSummaryProjection> loadLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType) {
        return letterBoxQueryRepository.fetchLetterSummariesByUserIdAndBoxType(userId, boxType, pageable);
    }

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return letterBoxJpaRepository.existsByLetterIdAndUserId(letterId, userId);
    }

    @Override
    public void delete(Long letterId, LetterType letterType, BoxType boxType) {
        letterBoxQueryRepository.deleteByCondition(List.of(letterId), letterType, boxType);
    }

    @Override
    public void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        letterBoxQueryRepository.deleteByCondition(letterIds, letterType, boxType);
    }

    @Override
    public void deleteAllByUserIdAndBoxType(Long userId, BoxType boxType) {
        letterBoxQueryRepository.deleteAllByUserIdAndBoxType(userId, boxType);
    }

    @Override
    public void deleteByConditionAndUserId(List<Long> ids, LetterType letterType, BoxType boxType, Long userId) {
        letterBoxQueryRepository.deleteByConditionAndUserId(ids, LetterType.LETTER, boxType, userId);
    }

    @Override
    public long countLetters(Long userId, BoxType boxType) {
        return letterBoxQueryRepository.countLetters(userId, boxType);
    }

    private void save(Long letterId, Long userId, LetterType letterType, BoxType boxType, LocalDateTime createdAt) {
        letterBoxJpaRepository.save(
                LetterBoxEntity.from(LetterBox.create(letterId, userId, letterType, boxType, createdAt))).toDomain();
    }
}
