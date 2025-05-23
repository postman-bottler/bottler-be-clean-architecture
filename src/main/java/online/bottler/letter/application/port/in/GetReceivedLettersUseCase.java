package online.bottler.letter.application.port.in;

import jakarta.validation.Valid;
import online.bottler.letter.adapter.in.web.request.PageRequest;
import online.bottler.letter.application.response.LetterSummaryResponse;
import org.springframework.data.domain.Page;

public interface GetReceivedLettersUseCase {
    Page<LetterSummaryResponse> getReceivedLetters(@Valid PageRequest pageRequest, Long userId);
}
