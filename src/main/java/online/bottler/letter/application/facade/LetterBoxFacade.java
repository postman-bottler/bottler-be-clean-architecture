package online.bottler.letter.application.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.domain.BoxType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LetterBoxFacade {

    private final LetterBoxUseCase letterBoxUseCase;

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getAllLetters(CommonPageCommand commonPageCommand, Long userId) {
        List<LetterSummaryResponse> responses = LetterSummaryResponse.fromList(
                letterBoxUseCase.getAllLetters(commonPageCommand, userId));
        long count = letterBoxUseCase.countLetters(userId, BoxType.NONE);
        return new PageImpl<>(responses, commonPageCommand.toPageable(), count);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getReceivedLetters(CommonPageCommand commonPageCommand, Long userId) {
        List<LetterSummaryResponse> responses = LetterSummaryResponse.fromList(
                letterBoxUseCase.getReceivedLetters(commonPageCommand, userId));
        long count = letterBoxUseCase.countLetters(userId, BoxType.RECEIVE);
        // new PageImpl 메서드 분리 ㄱㅊ
        return new PageImpl<>(responses, commonPageCommand.toPageable(), count);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> getSentLetters(CommonPageCommand commonPageCommand, Long userId) {
        List<LetterSummaryResponse> responses = LetterSummaryResponse.fromList(
                letterBoxUseCase.getSentLetters(commonPageCommand, userId));
        long count = letterBoxUseCase.countLetters(userId, BoxType.SEND);
        return new PageImpl<>(responses, commonPageCommand.toPageable(), count);
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
