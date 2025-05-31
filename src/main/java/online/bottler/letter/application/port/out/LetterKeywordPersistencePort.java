package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;

public interface LetterKeywordPersistencePort {
    void createAll(List<LetterKeyword> letterKeywords);

    void softDelete(Long letterId);

    void softDeleteByIds(List<Long> ids);

    List<String> loadFrequentKeywords(List<Long> letterIds);

    List<LetterKeyword> loadKeywordsByLetterId(Long letterId);

    List<Long> loadMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit);
}
