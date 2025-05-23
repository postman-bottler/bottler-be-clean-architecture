package online.bottler.mapletter.adaptor.in.web.request;

import java.util.List;
import online.bottler.mapletter.application.command.DeleteReplyMapLettersCommand;

public record DeleteReplyMapLettersRequest(
        List<Long> letterIds
) {
    public DeleteReplyMapLettersCommand toCommand() {
        return new DeleteReplyMapLettersCommand(letterIds);
    }
}
