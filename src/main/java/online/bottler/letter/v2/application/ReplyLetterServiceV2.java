package online.bottler.letter.v2.application;


import lombok.RequiredArgsConstructor;
import online.bottler.letter.v2.application.command.ReplyLetterCommand;
import online.bottler.letter.v2.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.v2.application.port.in.CreateReplyLetterUseCase;
import online.bottler.letter.v2.application.port.in.GetReplyLetterDetailUseCase;
import online.bottler.letter.v2.application.port.in.GetReplyLetterSummariesUseCase;
import online.bottler.letter.v2.application.response.ReplyLetterDetailResponse;
import online.bottler.letter.v2.application.response.ReplyLetterResponse;
import online.bottler.letter.v2.application.response.ReplyLetterSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyLetterServiceV2 implements CreateReplyLetterUseCase, GetReplyLetterSummariesUseCase,
        GetReplyLetterDetailUseCase {

    @Override
    public ReplyLetterResponse create(ReplyLetterCommand command) {
        return null;
    }

    @Override
    public ReplyLetterDetailResponse getDetail(Long replyLetterId, Long userId) {
        return null;
    }

    @Override
    public Page<ReplyLetterSummaryResponse> getSummaries(ReplyLetterSummariesQuery replyLetterSummariesQuery) {
        return null;
    }
}
