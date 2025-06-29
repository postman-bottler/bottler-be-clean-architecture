package online.bottler.letter.application.command;

import online.bottler.letter.domain.BoxType;

public record ReplyLetterDeleteCommand(Long id, Long userId, BoxType boxType) {
    public static ReplyLetterDeleteCommand of(Long id, Long userId, String boxType) {
        return new ReplyLetterDeleteCommand(id, userId, BoxType.valueOf(boxType));
    }
}
