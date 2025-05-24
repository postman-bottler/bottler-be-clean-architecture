package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.application.response.ReplyLetterDetailResponse;

public interface GetReplyLetterDetailUseCase {
    ReplyLetterDetailResponse getDetail(Long replyLetterId, Long userId);
}
