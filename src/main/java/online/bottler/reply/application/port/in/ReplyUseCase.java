package online.bottler.reply.application.port.in;

import java.util.List;
import online.bottler.reply.application.response.ReplyResponse;

public interface ReplyUseCase {
    List<ReplyResponse> findRecentReplyLetters(Long userId);
}
