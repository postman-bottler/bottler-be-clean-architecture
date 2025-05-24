package online.bottler.mapletter.adaptor.in.web.request;

import java.util.List;
import online.bottler.mapletter.application.command.DeleteArchivedLettersCommand;

public record DeleteArchivedLettersRequest(
        List<Long> letterIds
) {
    public DeleteArchivedLettersCommand toCommand() {
        return new DeleteArchivedLettersCommand(letterIds);
    }
}
