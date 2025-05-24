package online.bottler.letter.application;


import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.GetFrequentKeywordsUseCase;
import online.bottler.letter.application.response.FrequentKeywordsDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterKeywordServiceV2 implements GetFrequentKeywordsUseCase {

    @Override
    public FrequentKeywordsDTO getTopFrequent(Long userId) {
        return null;
    }
}
