package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import online.bottler.letter.application.repository.LetterKeywordRepository;
import online.bottler.letter.application.repository.LetterRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {

    private final LetterKeywordRepository letterKeywordRepository;
    private final LetterRepository letterRepository;

    public List<Long> getRecommendedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        log.debug("추천 편지 조회 요청: userKeywords={}, 제외할 letterIds={}, 추천 개수 limit={}", userKeywords, letterIds, limit);

        List<Long> recommendedLetters = letterKeywordRepository.getMatchedLetters(userKeywords, letterIds, limit);

        if (recommendedLetters.size() < limit) {
            int remaining = limit - recommendedLetters.size();
            List<Long> randomLetters = letterRepository.getRandomIds(remaining, letterIds);
            recommendedLetters.addAll(randomLetters);
        }

        if (recommendedLetters.isEmpty()) {
            log.warn("추천할 편지가 없음: userKeywords={}", userKeywords);
        } else {
            log.info("추천 편지 조회 완료: 추천된 편지 개수={}, userKeywords={}", recommendedLetters.size(), userKeywords);
        }

        return recommendedLetters;
    }
}
