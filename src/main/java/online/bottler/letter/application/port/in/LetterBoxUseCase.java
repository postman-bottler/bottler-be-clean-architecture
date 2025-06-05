package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.adapter.in.web.request.CommonPageRequest;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.application.response.LetterSummaryResponse;
import org.springframework.data.domain.Page;

public interface LetterBoxUseCase {
    void save(List<Long> letterId, Long userId);

    Page<LetterSummaryResponse> getAllLetters(CommonPageRequest commonPageRequest, Long userId);

    Page<LetterSummaryResponse> getReceivedLetters(CommonPageRequest commonPageRequest, Long userId);

    Page<LetterSummaryResponse> getSentLetters(CommonPageRequest commonPageRequest, Long userId);

    void deleteLetters(List<LetterDeleteCommand> letterDeleteCommands, Long userId);

    void deleteAllLetters(Long userId);

    void deleteAllReceivedLetters(Long userId);

    void deleteAllSentLetters(Long userId);
}
