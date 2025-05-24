package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.GetRecommendedLettersUseCase;
import online.bottler.letter.application.port.out.LoadLetterPersistencePort;
import online.bottler.letter.application.port.out.LoadRecommendedLetterIdsCachePort;
import online.bottler.letter.application.response.LetterRecommendSummaryResponse;
import online.bottler.letter.domain.Letter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendedLetterServiceV2 implements GetRecommendedLettersUseCase {
    
    private final LoadLetterPersistencePort loadLetterPersistencePort;
    private final LoadRecommendedLetterIdsCachePort loadRecommendedLetterIdsCachePort;
    
    @Override
    @Transactional(readOnly = true)
    public List<LetterRecommendSummaryResponse> getRecommended(Long userId) {
        List<Long> letterIds = loadRecommendedLetterIdsCachePort.fetchActiveByUserId(userId);
        List<Letter> letters = loadLetterPersistencePort.loadAllByIds(letterIds);
        return LetterRecommendSummaryResponse.fromList(letters);
    }
}
