package online.bottler.letter.application.service;


import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.application.port.in.LetterKeywordUseCase;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.domain.LetterStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterKeywordService implements LetterKeywordUseCase {

    private final LetterKeywordPersistencePort letterKeywordPersistencePort;
    private final LetterPersistencePort letterPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public List<String> getMostFrequentKeywords(Long userId) {
        List<Long> letterIds = letterPersistencePort.loadIdsByUserIdAndStatus(userId, LetterStatus.OPEN);

        if (letterIds.isEmpty()) {
            log.warn("사용자의 편지 ID가 없음: userId={}", userId);
            return Collections.emptyList();
        }

        return letterKeywordPersistencePort.loadFrequentKeywords(letterIds);
    }
}
