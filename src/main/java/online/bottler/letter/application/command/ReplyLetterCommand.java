package online.bottler.letter.application.command;

import online.bottler.letter.domain.LetterContent;

public record ReplyLetterCommand(Long letterId, Long userId, LetterContent letterContent) {
    public static ReplyLetterCommand of(Long letterId, Long userId, String content, String font, String paper, String label) {
        return new ReplyLetterCommand(letterId, userId, LetterContent.of(null, content, font, paper, label));
    }
}
