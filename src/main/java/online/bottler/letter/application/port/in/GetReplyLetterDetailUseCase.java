package online.bottler.letter.application.port.in;

import online.bottler.letter.application.response.ReplyLetterDetailResponse;

public interface GetReplyLetterDetailUseCase {
    ReplyLetterDetailResponse getDetail(Long replyLetterId, Long userId);
}
