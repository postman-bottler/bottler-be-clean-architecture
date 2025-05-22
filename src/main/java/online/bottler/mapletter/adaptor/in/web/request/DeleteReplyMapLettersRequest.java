package online.bottler.mapletter.adaptor.in.web.request;

import java.util.List;
import online.bottler.mapletter.application.command.DeleteReplyMapLettersCommand;

public record DeleteReplyMapLettersRequest(
        List<Long> letterIds
) {
    public static DeleteReplyMapLettersCommand toCommand(DeleteReplyMapLettersRequest request) {
        return new DeleteReplyMapLettersCommand(request.letterIds);
    }
}
