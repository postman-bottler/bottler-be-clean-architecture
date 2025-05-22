package online.bottler.mapletter.adaptor.in.web.request;

import java.util.List;
import online.bottler.mapletter.application.command.DeleteArchivedLettersCommand;

public record DeleteArchivedLettersRequest(
        List<Long> letterIds
) {
    public static DeleteArchivedLettersCommand toCommand(DeleteArchivedLettersRequest request) {
        return new DeleteArchivedLettersCommand(request.letterIds);
    }
}
