package online.bottler.letter.exception;

import online.bottler.global.response.code.ErrorStatus;

public class TempRecommendationsNotFoundException extends LetterCustomException {
    public TempRecommendationsNotFoundException() {
        super(ErrorStatus.TEMP_RECOMMENDATIONS_NOT_FOUND, "추천 데이터가 없습니다.");
    }
}
