package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.in.web.request.PageRequest;
import online.bottler.letter.application.command.LetterBoxCommand;
import online.bottler.letter.application.port.in.CreateLetterBoxUseCase;
import online.bottler.letter.application.port.in.GetAllLettersUseCase;
import online.bottler.letter.application.port.in.GetReceivedLetterIdsUseCase;
import online.bottler.letter.application.port.in.GetReceivedLettersUseCase;
import online.bottler.letter.application.port.in.GetSentLettersUseCase;
import online.bottler.letter.application.response.LetterSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterBoxServiceV2 implements CreateLetterBoxUseCase, GetAllLettersUseCase, GetSentLettersUseCase, GetReceivedLettersUseCase,
        GetReceivedLetterIdsUseCase {

    @Override
    public void create(LetterBoxCommand letterBoxCommand) {

    }

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

    @Override
    public List<Long> getReceivedLetterIdsByUserId(String userId) {
        return List.of();
    }
}
