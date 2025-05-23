package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.adapter.out.persistence.repository.LetterBoxJpaRepository;
import online.bottler.letter.adapter.out.persistence.repository.LetterBoxQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.application.port.out.LetterBoxRepository;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.adapter.out.persistence.entity.LetterBoxEntity;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LetterBoxRepositoryImpl implements LetterBoxRepository {

    private final LetterBoxQueryRepository letterBoxQueryRepository;
    private final LetterBoxJpaRepository letterBoxJpaRepository;

    @Override
    public void save(LetterBox letterBox) {
        letterBoxJpaRepository.save(LetterBoxEntity.from(letterBox));
    }

    @Override
    public List<Long> findReceivedLetterIdsByUserId(Long userId) {
        return letterBoxQueryRepository.findReceivedLetterIdsByUserId(userId);
    }

    @Override
    public Page<LetterSummaryResponse> findLetters(Long userId, Pageable pageable, BoxType boxType) {
        List<LetterSummaryResponse> letterSummaryResponses = letterBoxQueryRepository.fetchLetters(userId,
                boxType, pageable);
        long total = countLetters(userId, boxType);
        return new PageImpl<>(letterSummaryResponses, pageable, total);
    }

    @Override
    public void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        letterBoxQueryRepository.deleteByCondition(letterIds, letterType, boxType);
    }

    @Override
    public void deleteByConditionAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId) {
        letterBoxQueryRepository.deleteByConditionAndUserId(letterIds, letterType, boxType, userId);
    }

    @Override
    public void deleteAllByBoxTypeForUser(Long userId, BoxType boxType) {
        letterBoxQueryRepository.deleteAllByUserIdAndBoxType(userId, boxType);
    }


    @Override
    public boolean existsByUserIdAndLetterId(Long userId, Long letterId) {
        return letterBoxJpaRepository.existsByUserIdAndLetterId(userId, letterId);
    }

    private long countLetters(Long userId, BoxType boxType) {
        return letterBoxQueryRepository.countLetters(userId, boxType);
    }
}
