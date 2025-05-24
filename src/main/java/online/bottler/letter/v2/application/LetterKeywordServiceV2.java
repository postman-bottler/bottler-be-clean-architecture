package online.bottler.letter.v2.application;


import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.dto.response.FrequentKeywordsDTO;
import online.bottler.letter.v2.application.port.in.GetFrequentKeywordsUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterKeywordServiceV2 implements GetFrequentKeywordsUseCase {

    @Override
    public FrequentKeywordsDTO getTopFrequent(Long userId) {
        return null;
    }
}
