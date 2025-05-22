package online.bottler.letter.application;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.LetterDeleteDTO;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.port.in.CreateLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.DeleteLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.GetLetterWithKeywordsDetailUseCase;
import online.bottler.letter.application.response.LetterDetailResponse;
import online.bottler.letter.application.response.LetterResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterWithKeywordsService implements CreateLetterWithKeywordsUseCase, GetLetterWithKeywordsDetailUseCase,
        DeleteLetterWithKeywordsUseCase {
    @Override
    public LetterResponse create(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        return null;
    }

    @Override
    public LetterDetailResponse getDetail(Long userId, Long letterId) {
        return null;
    }

    @Override
    public void delete(LetterDeleteDTO letterDeleteDTO, Long userId) {

    }
}
