package online.bottler.letter.application.port.in;

import java.util.List;

public interface LetterKeywordUseCase {
    List<String> getMostFrequentKeywords(Long userId);
}
