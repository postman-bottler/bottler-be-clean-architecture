package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;
import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.application.response.ReplyLetterDetailResponse;
import online.bottler.letter.application.response.ReplyLetterResponse;
import online.bottler.letter.application.response.ReplyLetterSummaryResponse;
import org.springframework.data.domain.Page;

public interface ReplyLetterUseCase {
    ReplyLetterResponse create(ReplyLetterCommand command);

    Page<ReplyLetterSummaryResponse> getSummaries(ReplyLetterSummariesQuery query);

    ReplyLetterDetailResponse getDetail(Long id, Long userId);

    void softDelete(ReplyLetterDeleteCommand command);
}
