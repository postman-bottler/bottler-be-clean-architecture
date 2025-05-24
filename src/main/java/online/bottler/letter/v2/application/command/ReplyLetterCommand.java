package online.bottler.letter.v2.application.command;

import online.bottler.letter.domain.LetterContent;

public record ReplyLetterCommand(Long letterId, Long userId, LetterContent letterContent) {
    public static ReplyLetterCommand of(Long letterId, Long userId, LetterContent letterContent) {
        return new ReplyLetterCommand(letterId, userId, letterContent);
    }
}
