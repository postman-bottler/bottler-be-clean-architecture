package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.GetRecommendedLettersUseCase;
import online.bottler.letter.application.port.out.LoadLetterPort;
import online.bottler.letter.application.port.out.LoadRecommendedLetterIdsCachePort;
import online.bottler.letter.application.response.LetterRecommendSummaryResponse;
import online.bottler.letter.domain.Letter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendedLetterServiceV2 implements GetRecommendedLettersUseCase {
    
    private final LoadLetterPort loadLetterPort;
    private final LoadRecommendedLetterIdsCachePort loadRecommendedLetterIdsCachePort;
    
    @Override
    public List<LetterRecommendSummaryResponse> getRecommended(Long userId) {
        List<Long> letterIds = loadRecommendedLetterIdsCachePort.fetchActiveByUserId(userId);
        List<Letter> letters = loadLetterPort.loadAllByIds(letterIds);
        return LetterRecommendSummaryResponse.fromList(letters);
    }
}
