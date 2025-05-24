package online.bottler.letter.v2.application.port.in;

import jakarta.validation.Valid;
import online.bottler.letter.v2.application.response.LetterSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GetSentLettersUseCase {
    Page<LetterSummaryResponse> getSentLetters(@Valid PageRequest pageRequestDTO, Long userId);
}
