package online.bottler.letter.application.service;


import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.application.port.in.LetterKeywordUseCase;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterKeywordService implements LetterKeywordUseCase {

    private final LetterKeywordPersistencePort letterKeywordPersistencePort;

    @Transactional(readOnly = true)
    @Override
    public List<String> getTopFrequent(List<Long> letterIds, Long userId) {
        if (letterIds.isEmpty()) {
            log.warn("사용자의 편지 ID가 없음: userId={}", userId);
            return Collections.emptyList();
        }

        return letterKeywordPersistencePort.loadFrequentKeywords(letterIds);
    }
}
