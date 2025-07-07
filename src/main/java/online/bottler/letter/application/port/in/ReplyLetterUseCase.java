package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;
import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;

public interface ReplyLetterUseCase {
    ReplyLetter write(ReplyLetterCommand replyLetterCommand);

    Page<ReplyLetter> getPagedReplyLetters(ReplyLetterSummariesQuery query);

    ReplyLetter getReplyLetter(Long userId, Long id);

    ReplyLetter softDelete(ReplyLetterDeleteCommand command);

    boolean isReplied(Long userId, Long letterId);

    List<Long> getIdsByUserId(Long userId);

    void softDeleteByIds(List<Long> ids);

    List<ReplyLetter> getAllByIds(List<Long> ids);
}
