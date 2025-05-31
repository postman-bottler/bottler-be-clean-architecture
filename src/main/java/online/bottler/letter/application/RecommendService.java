package online.bottler.letter.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.application.port.in.RecommendUseCase;
import online.bottler.letter.application.port.out.LetterBoxPersistencePort;
import online.bottler.letter.application.port.out.LetterCachePort;
import online.bottler.letter.application.port.out.LetterKeywordPersistencePort;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.application.port.out.RecommendationCachePort;
import online.bottler.letter.application.port.out.RecommendedLetterPersistencePort;
import online.bottler.letter.application.port.out.UserKeywordPersistencePort;
import online.bottler.letter.application.response.LetterRecommendSummaryResponse;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.RecommendedLetter;
import online.bottler.letter.exception.LetterNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService implements RecommendUseCase {

    @Value("${recommendation.limit.candidate}")
    private int recommendationCandidateLimit;

    private final RecommendationCachePort recommendationCachePort;
    private final LetterBoxPersistencePort letterBoxPersistencePort;
    private final LetterKeywordPersistencePort letterKeywordPersistencePort;
    private final LetterPersistencePort letterPersistencePort;
    private final LetterCachePort letterCachePort;
    private final UserKeywordPersistencePort userKeywordPersistencePort;
    private final RecommendedLetterPersistencePort recommendedLetterPersistencePort;

    @Override
    public void saveDeveloperLetter(Long userId, List<Long> recommendations) {
        recommendationCachePort.saveDeveloperLetter(userId, recommendations);
    }

    @Override
    public String generate(Long userId) {
        log.info("사용자 [{}]의 추천 작업을 시작합니다.", userId);

        try {
            List<String> keywords = userKeywordPersistencePort.loadKeywords(userId);
            List<Long> letterIds = recommendedLetterPersistencePort.findRecommendedLettersByUserId(userId);

            if (log.isDebugEnabled()) {
                log.debug("사용자 [{}]의 키워드: {}, 추천 받았던 편지: {}", userId, keywords, letterIds);
            }

            List<Long> recommendedLetters = getRecommendedLetters(keywords, letterIds, recommendationCandidateLimit);
            recommendationCachePort.saveTempRecommendations(userId, recommendedLetters);

            log.info("사용자 [{}]의 추천 작업이 성공적으로 완료되었습니다.", userId);
            return "Success: 사용자 [" + userId + "] 작업 완료";
        } catch (Exception e) {
            log.error("사용자 [{}]의 추천 작업 중 예기치 못한 예외 발생: {}", userId, e.getMessage(), e);
            return "Error: 사용자 [" + userId + "] 예외 발생";
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<LetterRecommendSummaryResponse> getRecommended(Long userId) {
        List<Long> letterIds = letterCachePort.fetchActiveByUserId(userId);
        if (letterIds == null || letterIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Letter> letters = letterPersistencePort.loadAllByIds(letterIds);
        if (letters.size() != letterIds.size()) {
            throw new LetterNotFoundException(LetterType.LETTER);
        }

        return LetterRecommendSummaryResponse.fromList(letters);
    }

    @Override
    public List<Long> fetchActiveRecommendations(Long userId) {
        return recommendationCachePort.fetchActiveRecommendations(userId);
    }

    @Override
    public List<Long> fetchTempRecommendations(Long userId) {
        return recommendationCachePort.fetchTempRecommendations(userId);
    }

    @Override
    public Optional<Long> updateFromTemp(Long userId) {
        List<Long> tempRecommendations = recommendationCachePort.fetchTempRecommendations(userId);

        Optional<Long> recommendId = findFirstValidLetter(tempRecommendations);
        if (recommendId.isEmpty()) {
            log.info("userId={}에 대한 유효한 추천이 없음. 추천을 건너뜁니다.", userId);
            return Optional.empty();
        }

        recommendationCachePort.updateActiveRecommendations(userId, recommendId.get());
        letterBoxPersistencePort.createForRecommendedLetter(recommendId.get(), userId);
        recommendedLetterPersistencePort.create(RecommendedLetter.create(userId, recommendId.get()));

        return recommendId;
    }

    private List<Long> getRecommendedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        log.debug("추천 편지 조회 요청: userKeywords={}, 제외할 letterIds={}, 추천 개수 limit={}", userKeywords, letterIds, limit);

        List<Long> recommendedLetters = letterKeywordPersistencePort.loadMatchedLetters(userKeywords, letterIds,
                limit);

        if (recommendedLetters.size() < limit) {
            int remaining = limit - recommendedLetters.size();
            List<Long> randomLetterIds = letterPersistencePort.fetchRandomLetterIdsExcluding(remaining,
                    letterIds);
            recommendedLetters.addAll(randomLetterIds);
        }

        if (recommendedLetters.isEmpty()) {
            log.warn("추천할 편지가 없음: userKeywords={}", userKeywords);
        } else {
            log.info("추천 편지 조회 완료: 추천된 편지 개수={}, userKeywords={}", recommendedLetters.size(), userKeywords);
        }

        return recommendedLetters;
    }

    private Optional<Long> findFirstValidLetter(List<Long> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            return Optional.empty();
        }

        List<Long> validLetters = recommendations.stream().filter(this::isValidLetter).toList();

        if (validLetters.isEmpty()) {
            log.warn("추천할 편지가 모두 삭제됨. 새로운 추천이 필요함.");
            return Optional.empty();
        }

        return Optional.of(validLetters.get(0));
    }

    private boolean isValidLetter(Long letterId) {
        return letterPersistencePort.existsById(letterId);
    }

}
