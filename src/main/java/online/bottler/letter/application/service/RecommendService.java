package online.bottler.letter.application.service;

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
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.RecommendedLetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generate(Long userId) {
        log.info("사용자 [{}]의 추천 작업을 시작합니다.", userId);

        try {
            List<String> userKeywords = loadUserKeywords(userId);

            List<Long> letterIds = findRecommendationLetters(userId);

            List<Long> recommendedLetters = getRecommendedLetters(userKeywords, letterIds, recommendationCandidateLimit);

            recommendationCachePort.saveTempRecommendations(userId, recommendedLetters);

            log.info("사용자 [{}]의 추천 작업이 성공적으로 완료되었습니다.", userId);
            return "Success: 사용자 [" + userId + "] 작업 완료";
        } catch (Exception e) {
            log.error("사용자 [{}]의 추천 작업 중 예기치 못한 예외 발생: {}", userId, e.getMessage(), e);

            return "Error: 사용자 [" + userId + "] 예외 발생";
        }
    }

    @Override
    public List<Long> getRecommended(Long userId) {
        List<Long> letterIds = letterCachePort.fetchActiveByUserId(userId);

        return letterIds == null ? Collections.emptyList() : letterIds;
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
    @Transactional
    public Optional<Long> updateRecommendationsFromTemp(Long userId) {
        Optional<Long> recommendId = findFirstValidLetter(fetchTempRecommendations(userId));

        if (recommendId.isEmpty()) {
            log.info("userId={}에 대한 유효한 추천이 없음. 추천을 건너뜁니다.", userId);
            return Optional.empty();
        }

        updateRecommendation(userId, recommendId.get());

        return recommendId;
    }

    private List<String> loadUserKeywords(Long userId) {
        return userKeywordPersistencePort.loadKeywordsByUserId(userId);
    }

    private List<Long> findRecommendationLetters(Long userId) {
        return recommendedLetterPersistencePort.findRecommendedLettersByUserId(userId);
    }

    private List<Long> getRecommendedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        log.debug("추천 편지 조회 요청: userKeywords={}, 제외할 letterIds={}, 추천 개수 limit={}", userKeywords, letterIds, limit);

        List<Long> recommendedLetters = letterKeywordPersistencePort.loadMatchedLetters(userKeywords, letterIds, limit);

        if (recommendedLetters.size() < limit) {
            recommendedLetters.addAll(getRandomLetterIds(letterIds, limit - recommendedLetters.size()));
        }

        if (recommendedLetters.isEmpty()) {
            log.warn("추천할 편지가 없음: userKeywords={}", userKeywords);
        }

        return recommendedLetters;
    }

    private List<Long> getRandomLetterIds(List<Long> excludedLetterIds, int remaining) {
        return letterPersistencePort.loadRandomLetterIdsByIdNotInAndStatus(excludedLetterIds, LetterStatus.OPEN, remaining);
    }

    private Optional<Long> findFirstValidLetter(List<Long> recommendations) {
        return recommendations == null ? Optional.empty() :
                recommendations.stream().filter(this::isValidLetter).findFirst();
    }

    private boolean isValidLetter(Long letterId) {
        return letterPersistencePort.existsByIdAndStatus(letterId, LetterStatus.OPEN);
    }

    private void updateRecommendation(Long userId, Long recommendId) {
        recommendationCachePort.updateActiveRecommendations(userId, recommendId);

        letterBoxPersistencePort.save(
                LetterBox.create(userId, recommendId, LetterBoxType.of(LetterType.LETTER, BoxType.RECEIVE))
        );

        recommendedLetterPersistencePort.create(RecommendedLetter.create(userId, recommendId));
    }
}
