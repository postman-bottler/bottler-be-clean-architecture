package online.bottler.mapletter.application.command;

import java.util.List;

public record DeleteArchivedLettersCommand(
        List<Long> letterIds
) {
}
