package online.bottler.letter.application.command;

import online.bottler.letter.domain.BoxType;

public record ReplyLetterDeleteCommand(Long userId, Long id, BoxType boxType) {
    public static ReplyLetterDeleteCommand of(Long userId, Long id, String boxType) {
        return new ReplyLetterDeleteCommand(userId, id, BoxType.valueOf(boxType));
    }
}
