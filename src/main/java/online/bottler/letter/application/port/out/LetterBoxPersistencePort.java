package online.bottler.letter.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterSummary;
import online.bottler.letter.domain.LetterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LetterBoxPersistencePort {
    void createForLetter(Long letterId, Long userId, LocalDateTime createdAt);

    void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt);

    void createForRecommendedLetter(Long letterId, Long userId);

    void createForDeveloperLetter(List<Long> letterId, Long userId);

    Page<LetterSummary> loadLetterBoxSummaries(Long userId, Pageable pageable, BoxType boxType);

    boolean existsByLetterIdAndUserId(Long letterId, Long userId);

    void delete(Long letterId, LetterType letterType, BoxType boxType);

    void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType);

    void deleteAllByUserIdAndBoxType(Long userId, BoxType boxType);

    void deleteByConditionAndUserId(List<Long> ids, LetterType letterType, BoxType boxType, Long userId);
}
