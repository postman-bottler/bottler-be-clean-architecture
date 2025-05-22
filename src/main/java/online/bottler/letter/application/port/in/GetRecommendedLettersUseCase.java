package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.response.LetterRecommendSummaryResponseDTO;

public interface GetRecommendedLettersUseCase {
    List<LetterRecommendSummaryResponseDTO> getRecommended(Long userId);
}
