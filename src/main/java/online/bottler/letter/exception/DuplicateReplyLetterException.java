package online.bottler.letter.exception;

import online.bottler.global.response.code.ErrorStatus;

public class DuplicateReplyLetterException extends LetterCustomException {
    public DuplicateReplyLetterException() {
        super(ErrorStatus.DUPLICATE_REPLY_LETTER, "이미 이 편지에 답장한 기록이 있습니다.");
    }
}
