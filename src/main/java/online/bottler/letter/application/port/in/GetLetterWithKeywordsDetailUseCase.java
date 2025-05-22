package online.bottler.letter.application.port.in;

import online.bottler.letter.application.response.LetterDetailResponse;

public interface GetLetterWithKeywordsDetailUseCase {
    LetterDetailResponse getDetail(Long userId, Long letterId);
}
