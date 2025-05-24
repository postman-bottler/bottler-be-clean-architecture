package online.bottler.mapletter.application.command;

import java.util.List;

public record DeleteReplyMapLettersCommand(
        List<Long> letterIds
) {
}
