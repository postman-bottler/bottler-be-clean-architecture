package online.bottler.letter.application.command;

import jakarta.validation.constraints.NotNull;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.adapter.in.web.request.LetterDeleteRequest;
import online.bottler.letter.adapter.in.web.request.ReplyLetterDeleteRequest;

public record LetterDeleteDTO(@NotNull(message = "Letter ID는 필수입니다.") Long letterId,
                              @NotNull(message = "Letter Type은 필수입니다.") LetterType letterType,
                              @NotNull(message = "Box Type은 필수입니다.") BoxType boxType) {
    public static LetterDeleteDTO fromLetter(LetterDeleteRequest letterDeleteRequest) {
        return new LetterDeleteDTO(letterDeleteRequest.letterId(), LetterType.LETTER,
                letterDeleteRequest.boxType());
    }

    public static LetterDeleteDTO fromReplyLetter(ReplyLetterDeleteRequest replyLetterDeleteRequest) {
        return new LetterDeleteDTO(replyLetterDeleteRequest.letterId(), LetterType.REPLY_LETTER,
                replyLetterDeleteRequest.boxType());
    }
}
