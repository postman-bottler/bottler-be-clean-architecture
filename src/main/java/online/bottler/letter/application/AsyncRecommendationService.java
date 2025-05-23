package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncRecommendationService {

    private final RecommendService recommendService;
    private final UserKeywordService userKeywordService;
    private final RedisLetterService redisLetterService;
    private final RecommendedLetterService recommendedLetterService;

    @Value("${recommendation.limit.candidate}")
    private int recommendationCandidateLimit;

    public String processRecommendationForUser(Long userId) {
        log.info("사용자 [{}]의 추천 작업을 시작합니다.", userId);

        try {
            List<String> keywords = userKeywordService.findKeywords(userId);
            List<Long> letterIds = recommendedLetterService.findRecommendedLetterIdsByUserId(userId);

            if (log.isDebugEnabled()) {
                log.debug("사용자 [{}]의 키워드: {}, 추천 받았던 편지: {}", userId, keywords, letterIds);
            }

            List<Long> recommendedLetters = recommendService.getRecommendedLetters(keywords, letterIds,
                    recommendationCandidateLimit);
            redisLetterService.saveTempRecommendations(userId, recommendedLetters);

            log.info("사용자 [{}]의 추천 작업이 성공적으로 완료되었습니다.", userId);
            return "Success: 사용자 [" + userId + "] 작업 완료";
        } catch (Exception e) {
            log.error("사용자 [{}]의 추천 작업 중 예기치 못한 예외 발생: {}", userId, e.getMessage(), e);
            return "Error: 사용자 [" + userId + "] 예외 발생";
        }
    }
}
