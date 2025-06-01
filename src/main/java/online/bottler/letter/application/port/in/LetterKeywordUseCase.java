package online.bottler.letter.application.port.in;

import online.bottler.letter.application.response.FrequentKeywordsResponse;

public interface LetterKeywordUseCase {
    FrequentKeywordsResponse getTopFrequent(Long userId);
}
