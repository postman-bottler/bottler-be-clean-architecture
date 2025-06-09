package online.bottler.letter.application;


import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.application.port.in.LetterKeywordUseCase;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.response.FrequentKeywordsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterKeywordService implements LetterKeywordUseCase {

    private final LetterPersistencePort letterPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;

    @Transactional(readOnly = true)
    @Override
    public FrequentKeywordsResponse getTopFrequent(Long userId) {
        List<Long> letterIds = letterPersistencePort.loadIdsByUserId(userId);
        if (letterIds.isEmpty()) {
            log.warn("사용자의 편지 ID가 없음: userId={}", userId);
            return FrequentKeywordsResponse.from(Collections.emptyList());
        }

        List<String> frequentKeywords = letterKeywordPersistencePort.loadFrequentKeywords(letterIds);
        return FrequentKeywordsResponse.from(frequentKeywords);
    }
}
