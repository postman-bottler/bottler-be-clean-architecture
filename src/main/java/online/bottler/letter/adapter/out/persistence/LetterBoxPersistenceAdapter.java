package online.bottler.letter.adapter.out.persistence;

import static online.bottler.letter.domain.BoxType.RECEIVE;
import static online.bottler.letter.domain.BoxType.SEND;
import static online.bottler.letter.domain.LetterType.LETTER;
import static online.bottler.letter.domain.LetterType.REPLY_LETTER;

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
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterBoxPersistenceAdapter implements LetterBoxPersistencePort {

    private final LetterBoxJpaRepository letterBoxJpaRepository;
    private final LetterBoxQueryRepository letterBoxQueryRepository;

    @Override
    public void createForLetter(Long letterId, Long userId, LocalDateTime createdAt) {
        save(letterId, userId, LetterBoxType.of(LETTER, SEND), createdAt);
    }

    @Override
    public void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt) {
        save(letterId, userId, LetterBoxType.of(REPLY_LETTER, SEND), createdAt);
        save(letterId, receiverId, LetterBoxType.of(REPLY_LETTER, RECEIVE), createdAt);
    }

    @Override
    public void createForRecommendedLetter(Long letterId, Long userId) {
        save(letterId, userId, LetterBoxType.of(LETTER, SEND), LocalDateTime.now());
    }

    @Override
    public void createForDeveloperLetter(List<Long> letterIds, Long userId) {
        letterIds.forEach(letterId -> save(letterId, userId, LetterBoxType.of(LETTER, RECEIVE), LocalDateTime.now()));
    }

    @Override
    public Page<LetterSummary> loadLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType) {
        Page<LetterSummaryProjection> letterSummaryProjections = letterBoxQueryRepository.fetchLetterSummariesByUserIdAndBoxType(
                userId, boxType, pageable);
        return letterSummaryProjections.map(LetterSummaryProjection::toDomain);
    }

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return letterBoxJpaRepository.existsByLetterIdAndUserId(letterId, userId);
    }

    @Override
    public void delete(Long letterId, LetterBoxType letterBoxType) {
        letterBoxQueryRepository.deleteByCondition(List.of(letterId), letterBoxType.getLetterType(), letterBoxType.getBoxType());
    }

    @Override
    public void deleteByCondition(List<Long> letterIds, LetterBoxType letterBoxType) {
        letterBoxQueryRepository.deleteByCondition(letterIds, letterBoxType.getLetterType(), letterBoxType.getBoxType());
    }

    @Override
    public void deleteAllByUserIdAndBoxType(Long userId, BoxType boxType) {
        letterBoxQueryRepository.deleteAllByUserIdAndBoxType(userId, boxType);
    }

    @Override
    public void deleteByConditionAndUserId(List<Long> ids, LetterBoxType letterBoxType, Long userId) {
        letterBoxQueryRepository.deleteByConditionAndUserId(ids, letterBoxType.getLetterType(), letterBoxType.getBoxType(), userId);
    }

    private void save(Long letterId, Long userId, LetterBoxType letterBoxType, LocalDateTime createdAt) {
        letterBoxJpaRepository.save(
                LetterBoxEntity.from(LetterBox.create(userId, letterId, letterBoxType)));
    }
}
