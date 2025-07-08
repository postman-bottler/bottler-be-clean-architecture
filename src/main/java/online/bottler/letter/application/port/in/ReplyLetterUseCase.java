package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;
import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;

public interface ReplyLetterUseCase {
    ReplyLetter write(ReplyLetterCommand replyLetterCommand);

    ReplyLetter getReplyLetter(Long userId, Long id);

    List<ReplyLetter> getReplyLetters(List<Long> ids);

    List<Long> getReplyLetterIds(Long userId);

    Page<ReplyLetter> getPagedReplyLetters(ReplyLetterSummariesQuery query);

    boolean isReplied(Long userId, Long letterId);

    ReplyLetter removeReplyLetter(ReplyLetterDeleteCommand command);

    void removeReplyLetters(List<Long> ids);
}
