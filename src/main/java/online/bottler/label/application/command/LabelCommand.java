package online.bottler.label.application.command;

import java.time.LocalDateTime;
import java.util.List;

public record LabelCommand(
        List<Long> labelIds,
        LocalDateTime scheduledDateTime
) {
}
