package online.bottler.letter.application.command;

import online.bottler.letter.domain.LetterContent;

public record ReplyLetterCommand(Long userId, Long letterId, LetterContent letterContent) {
    public static ReplyLetterCommand of(
            Long userId,
            Long letterId,
            String content, String font, String paper, String label
    ) {
        return new ReplyLetterCommand(
                userId,
                letterId,
                LetterContent.of(null, content, font, paper, label)
        );
    }
}
