package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.v2.application.response.ReplyLetterSummaryResponse;
import org.springframework.data.domain.Page;

public interface GetReplyLetterSummariesUseCase {
    Page<ReplyLetterSummaryResponse> getSummaries(ReplyLetterSummariesQuery replyLetterSummariesQuery);
}
