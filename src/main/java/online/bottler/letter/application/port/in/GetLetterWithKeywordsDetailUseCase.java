package online.bottler.letter.application.port.in;


import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.response.LetterWithKeywordsDetailResponse;

public interface GetLetterWithKeywordsDetailUseCase {
    LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery letterWithKeywordsDetailQuery);
}
