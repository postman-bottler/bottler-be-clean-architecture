package online.bottler.letter.exception;

import online.bottler.global.response.code.ErrorStatus;

public class InvalidSortFieldException extends LetterCustomException {
    public InvalidSortFieldException() {
        super(ErrorStatus.INVALID_SORT_FIELD, "유효하지 않은 값입니다.");
    }
}