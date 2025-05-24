package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.application.response.ReplyLetterSummaryResponse;
import org.springframework.data.domain.Page;

public interface GetReplyLetterSummariesUseCase {
    Page<ReplyLetterSummaryResponse> getSummaries(ReplyLetterSummariesQuery replyLetterSummariesQuery);
}
