package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.response.ReplyLetterResponse;

public interface CreateReplyLetterUseCase {
    ReplyLetterResponse create(ReplyLetterCommand command);
}
