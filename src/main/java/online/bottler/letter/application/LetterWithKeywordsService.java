package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.LetterDeleteDTO;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.port.in.CreateLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.DeleteLetterWithKeywordsUseCase;
import online.bottler.letter.application.port.in.GetLetterWithKeywordsDetailUseCase;
import online.bottler.letter.application.port.out.CreateLetterBoxPort;
import online.bottler.letter.application.port.out.CreateLetterKeywordPort;
import online.bottler.letter.application.port.out.CreateLetterPort;
import online.bottler.letter.application.response.LetterDetailResponse;
import online.bottler.letter.application.response.LetterWithKeywordsResponse;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.domain.LetterWithKeywords;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterWithKeywordsService implements CreateLetterWithKeywordsUseCase, GetLetterWithKeywordsDetailUseCase,
        DeleteLetterWithKeywordsUseCase {

    private final CreateLetterPort createLetterPort;
    private final CreateLetterKeywordPort createLetterKeywordPort;
    private final CreateLetterBoxPort createLetterBoxPort;

    @Override
    public LetterWithKeywordsResponse create(LetterWithKeywordsCommand letterWithKeywordsCommand) {
        Letter letter = createLetterPort.save(letterWithKeywordsCommand.toLetter());

        List<LetterKeyword> letterKeywords = LetterKeyword.createList(letter.getId(),
                letterWithKeywordsCommand.toKeywords());
        createLetterKeywordPort.saveAll(letterKeywords);

        createLetterBoxPort.saveLetter(letter.getUserId(), letter.getId(), letter.getCreatedAt());

        LetterWithKeywords letterWithKeywords = LetterWithKeywords.create(letter, letterWithKeywordsCommand.keywords());
        return LetterWithKeywordsResponse.from(letterWithKeywords);
    }

    @Override
    public LetterDetailResponse getDetail(Long userId, Long letterId) {
        return null;
    }

    @Override
    public void delete(LetterDeleteDTO letterDeleteDTO, Long userId) {

    }
}
