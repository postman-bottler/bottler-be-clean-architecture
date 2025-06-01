package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.KeywordUseCase;
import online.bottler.letter.application.port.out.KeywordPersistencePort;
import online.bottler.letter.application.response.KeywordResponse;
import online.bottler.letter.domain.Keyword;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordServiceV2 implements KeywordUseCase {

    private final KeywordPersistencePort keywordPersistencePort;

    @Override
    public KeywordResponse getAll() {
        List<Keyword> keywords = keywordPersistencePort.getKeywords();
        return KeywordResponse.from(keywords);
    }
}
