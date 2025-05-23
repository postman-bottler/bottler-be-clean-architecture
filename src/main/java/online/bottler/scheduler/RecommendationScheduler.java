package online.bottler.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import online.bottler.letter.application.AsyncRecommendationService;
import online.bottler.letter.application.RedisLetterService;
import online.bottler.letter.application.LetterService;
import online.bottler.notification.application.request.RecommendNotificationCommand;
import online.bottler.notification.application.NotificationService;
import online.bottler.user.application.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationScheduler {

    private final AsyncRecommendationService asyncRecommendationService;
    private final UserService userService;
    private final RedisLetterService redisLetterService;
    private final NotificationService notificationService;
    private final LetterService letterService;

    @Value("${scheduler.batch-size}")
    private int batchSize;

    @Value("${scheduler.parallelism}")
    private int parallelism;

    public void processAllUserRecommendations() {
        List<Long> userIds = userService.getAllUserIds();
        List<List<Long>> batches = createBatches(userIds);

        ExecutorService executorService = Executors.newFixedThreadPool(parallelism);

        try {
            for (List<Long> batch : batches) {
                log.info("사용자 배치 처리 시작 (크기: {}): {}", batch.size(), batch);

                List<CompletableFuture<String>> futures = batch.stream().map(userId -> CompletableFuture.supplyAsync(
                                () -> asyncRecommendationService.processRecommendationForUser(userId), executorService))
                        .toList();

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .thenRun(() -> futures.forEach(this::handleFutureResult))
                        .exceptionally(ex -> {
                            log.error("배치 처리 중 예외 발생: {}", ex.getMessage(), ex);
                            return null;
                        }).join();

                log.info("사용자 배치 처리 완료: {}", batch);
            }
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    log.warn("ExecutorService가 30초 내에 종료되지 않아 강제 종료합니다.");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.error("ExecutorService 종료 중 인터럽트 발생: {}", ie.getMessage(), ie);
            }
        }
    }

    public void updateAllRecommendations() {
        List<Long> userIds = userService.getAllUserIds();
        List<List<Long>> batches = createBatches(userIds);

        List<RecommendNotificationCommand> notifications = new ArrayList<>();
        for (List<Long> batch : batches) {
            batch.forEach(userId -> redisLetterService.updateRecommendationsFromTemp(userId)
                    .ifPresent(recommendId -> notifications.add(createRecommendNotification(userId, recommendId))));
        }

        if (!notifications.isEmpty()) {
            try {
                notificationService.sendKeywordNotifications(notifications);
                log.info("추천 알림이 성공적으로 전송되었습니다. 알림 수: {}", notifications.size());
            } catch (Exception e) {
                log.error("추천 알림 전송 중 예외 발생: {}", e.getMessage(), e);
            }
        }
    }

    private List<List<Long>> createBatches(List<Long> items) {
        List<List<Long>> batches = new ArrayList<>();
        for (int i = 0; i < items.size(); i += batchSize) {
            batches.add(items.subList(i, Math.min(items.size(), i + batchSize)));
        }
        return batches;
    }

    private RecommendNotificationCommand createRecommendNotification(Long userId, Long recommendId) {
        return RecommendNotificationCommand.of(userId, recommendId, letterService.findLetter(recommendId).getLabel());
    }

    private void handleFutureResult(CompletableFuture<String> future) {
        try {
            log.info("작업 결과: {}", future.get());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.error("작업 결과를 가져오는 중 인터럽트 발생: {}", ie.getMessage(), ie);
        } catch (ExecutionException ee) {
            log.error("작업 결과를 가져오는 중 실행 예외 발생: {}", ee.getMessage(), ee);
        } catch (Exception ex) {
            log.error("작업 결과를 가져오는 중 알 수 없는 예외 발생: {}", ex.getMessage(), ex);
        }
    }
}
