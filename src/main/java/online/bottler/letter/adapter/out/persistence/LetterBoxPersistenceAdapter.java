package online.bottler.letter.adapter.out.persistence;

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
    public void save(LetterBox letterBox) {
        letterBoxJpaRepository.save(LetterBoxEntity.from(letterBox));
    }

    @Override
    public Page<LetterSummary> loadLetterBoxSummaries(Long userId, BoxType boxType, Pageable pageable) {
        Page<LetterSummaryProjection> letterSummaryProjections = letterBoxQueryRepository.fetchLetterSummariesByUserIdAndBoxType(
                userId, boxType, pageable);
        return letterSummaryProjections.map(LetterSummaryProjection::toDomain);
    }

    @Override
    public boolean existsByUserIdAndLetterId(Long userId, Long letterId) {
        return letterBoxJpaRepository.existsByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public void deleteLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType) {
        letterBoxQueryRepository.deleteLetters(userId, letterIds, letterBoxType.letterType(), letterBoxType.boxType());
    }
}
