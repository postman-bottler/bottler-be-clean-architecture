package online.bottler.letter.application.repository;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;

public interface LetterKeywordRepository {
    List<LetterKeyword> saveAll(List<LetterKeyword> letterKeywords);

    List<LetterKeyword> getKeywordsByLetterId(Long userId);

    List<Long> getMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit);

    void markKeywordsAsDeleted(List<Long> letterIds);

    List<String> getFrequentKeywords(List<Long> letterIds);
}
