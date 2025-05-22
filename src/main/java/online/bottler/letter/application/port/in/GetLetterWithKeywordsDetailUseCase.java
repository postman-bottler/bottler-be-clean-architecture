package online.bottler.letter.application.port.in;

import online.bottler.letter.application.response.LetterDetailResponseDTO;

public interface GetLetterWithKeywordsDetailUseCase {
    LetterDetailResponseDTO getDetail(Long userId, Long letterId);
}
