package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LetterBoxPersistencePort {

    void save(LetterBox letterBox);

    Page<LetterSummary> loadLetterBoxSummaries(Long userId, BoxType boxType, Pageable pageable);

    boolean existsByUserIdAndLetterId(Long userId, Long letterId);

    void deleteLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType);
}
