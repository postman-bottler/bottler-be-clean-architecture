package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterStatus;

public interface LetterKeywordPersistencePort {
    void createAll(List<LetterKeyword> letterKeywords);

    List<String> loadFrequentKeywords(List<Long> letterIds);

    List<LetterKeyword> loadKeywordsByLetterId(Long letterId);

    List<Long> loadMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit);

    List<LetterKeyword> loadAllByLetterId(Long letterId);

    List<LetterKeyword> loadAllByLetterIdInAndStatus(List<Long> ids, LetterStatus status);
}
