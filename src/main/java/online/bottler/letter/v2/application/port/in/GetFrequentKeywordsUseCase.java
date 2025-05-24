package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.application.dto.response.FrequentKeywordsDTO;

public interface GetFrequentKeywordsUseCase {
    FrequentKeywordsDTO getTopFrequent(Long userId);
}
