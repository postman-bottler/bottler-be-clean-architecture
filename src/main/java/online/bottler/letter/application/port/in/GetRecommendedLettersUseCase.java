package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.response.LetterRecommendSummaryResponse;

public interface GetRecommendedLettersUseCase {
    List<LetterRecommendSummaryResponse> getRecommended(Long userId);
}
