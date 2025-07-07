package online.bottler.letter.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LetterBoxPersistencePort {

    void save(LetterBox letterBox);

    void createForLetter(Long letterId, Long userId, LocalDateTime createdAt);

    void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt);

    void createForRecommendedLetter(Long letterId, Long userId);

    void createForDeveloperLetter(List<Long> letterId, Long userId);

    Page<LetterSummary> loadLetterBoxSummaries(Long userId, BoxType boxType, Pageable pageable);

    boolean existsByUserIdAndLetterId(Long userId, Long letterId);

    void deleteLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType);
}
