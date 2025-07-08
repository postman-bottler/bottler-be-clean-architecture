package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterStatus;

public interface LetterKeywordPersistencePort {
    void saveAll(List<LetterKeyword> letterKeywords);

    List<LetterKeyword> loadAllByLetterId(Long letterId);

    List<LetterKeyword> loadAllByLetterIdInAndStatus(List<Long> ids, LetterStatus status);

    List<String> loadKeywordsByLetterIdAndStatus(Long letterId, LetterStatus status);

    List<String> loadFrequentKeywords(List<Long> letterIds);

    List<Long> loadMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit);
}
