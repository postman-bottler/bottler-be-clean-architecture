package online.bottler.letter.application.port.in;

import online.bottler.letter.application.response.FrequentKeywordsDTO;

public interface GetFrequentKeywordsUseCase {
    FrequentKeywordsDTO getTopFrequent(Long userId);
}
