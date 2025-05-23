package online.bottler.letter.application;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.in.web.request.PageRequest;
import online.bottler.letter.application.port.in.GetAllLettersUseCase;
import online.bottler.letter.application.port.in.GetReceivedLettersUseCase;
import online.bottler.letter.application.port.in.GetSentLettersUseCase;
import online.bottler.letter.application.response.LetterSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterBoxServiceV2 implements GetAllLettersUseCase, GetSentLettersUseCase, GetReceivedLettersUseCase {

    @Override
    public Page<LetterSummaryResponse> getAllLetters(PageRequest pageRequestDTO, Long userId) {
        return null;
    }

    @Override
    public Page<LetterSummaryResponse> getReceivedLetters(PageRequest pageRequest, Long userId) {
        return null;
    }

    @Override
    public Page<LetterSummaryResponse> getSentLetters(PageRequest pageRequestDTO, Long userId) {
        return null;
    }
}
