package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.application.command.ReplyLetterCommand;
import online.bottler.letter.v2.application.response.ReplyLetterResponse;

public interface CreateReplyLetterUseCase {
    ReplyLetterResponse create(ReplyLetterCommand command);
}
