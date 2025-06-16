package online.bottler.letter.application.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.response.LetterSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LetterBoxFacade {

    private final LetterBoxUseCase letterBoxUseCase;

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getAllLetters(CommonPageCommand commonPageCommand, Long userId) {
        return letterBoxUseCase.getAllLetters(commonPageCommand, userId).map(LetterSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getReceivedLetters(CommonPageCommand commonPageCommand, Long userId) {
        return letterBoxUseCase.getReceivedLetters(commonPageCommand, userId).map(LetterSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getSentLetters(CommonPageCommand commonPageCommand, Long userId) {
        return letterBoxUseCase.getSentLetters(commonPageCommand, userId).map(LetterSummaryResponse::from);
    }

    @Transactional
    public void deleteLetters(List<LetterDeleteCommand> letterDeleteCommands, Long userId) {
        letterBoxUseCase.deleteLetters(letterDeleteCommands, userId);
    }

    @Transactional
    public void deleteAllLetters(Long userId) {
        letterBoxUseCase.deleteAllLetters(userId);
    }

    @Transactional
    public void deleteAllReceivedLetters(Long userId) {
        letterBoxUseCase.deleteAllReceivedLetters(userId);
    }

    @Transactional
    public void deleteAllSentLetters(Long userId) {
        letterBoxUseCase.deleteAllSentLetters(userId);
    }
}
