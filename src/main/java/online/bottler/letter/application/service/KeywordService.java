package online.bottler.letter.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.KeywordUseCase;
import online.bottler.letter.application.port.out.KeywordPersistencePort;
import online.bottler.letter.domain.Keyword;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeywordService implements KeywordUseCase {

    private final KeywordPersistencePort keywordPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public List<Keyword> getKeywords() {
        return keywordPersistencePort.loadAll();
    }
}
