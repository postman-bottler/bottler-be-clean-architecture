package online.bottler.letter.v2.application.port.in;

import online.bottler.letter.v2.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.v2.application.response.LetterWithKeywordsDetailResponse;

public interface GetLetterWithKeywordsDetailUseCase {
    LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery);
}
