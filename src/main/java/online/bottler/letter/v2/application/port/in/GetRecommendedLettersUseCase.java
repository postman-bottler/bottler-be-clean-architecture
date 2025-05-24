package online.bottler.letter.v2.application.port.in;


import java.util.List;
import online.bottler.letter.v2.application.response.LetterRecommendSummaryResponse;

public interface GetRecommendedLettersUseCase {
    List<LetterRecommendSummaryResponse> getRecommended(Long userId);
}
