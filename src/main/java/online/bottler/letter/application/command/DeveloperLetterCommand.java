package online.bottler.letter.application.command;

import java.util.List;

public record DeveloperLetterCommand(Long userId, List<Long> recommendations) {
    public static DeveloperLetterCommand of(final Long userId, final List<Long> recommendations) {
        return new DeveloperLetterCommand(userId, recommendations);
    }
}
