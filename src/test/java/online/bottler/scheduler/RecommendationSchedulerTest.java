package online.bottler.scheduler;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import online.bottler.letter.application.AsyncRecommendationService;
import online.bottler.letter.application.RedisLetterService;
import online.bottler.notification.application.request.RecommendNotificationCommand;
import online.bottler.notification.application.NotificationService;
import online.bottler.user.application.UserService;

@ExtendWith(MockitoExtension.class)
class RecommendationSchedulerTest {

    @InjectMocks
    private RecommendationScheduler recommendationScheduler;

    @Mock
    private RedisLetterService redisLetterService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AsyncRecommendationService asyncRecommendationService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("모든 사용자에 대해 추천 처리 실행")
    void processAllUserRecommendations() {
        // Given
        List<Long> userIds = List.of(1L, 2L, 3L);
        when(userService.getAllUserIds()).thenReturn(userIds);

        // When
        recommendationScheduler.processAllUserRecommendations();

        // Then
        verify(userService, times(1)).getAllUserIds();
        verify(asyncRecommendationService, times(3)).processRecommendationForUser(anyLong());
    }

    @Test
    @DisplayName("모든 사용자에 대한 추천 업데이트 및 알림 전송")
    void updateAllRecommendations() {
        // Given
        List<Long> userIds = List.of(1L, 2L);
        RecommendNotificationCommand mockNotification = new RecommendNotificationCommand(1L, 101L, "label1");

        when(userService.getAllUserIds()).thenReturn(userIds);
//        when(redisLetterService.updateRecommendationsFromTemp(anyLong())).thenReturn(mockNotification);

        // When
        recommendationScheduler.updateAllRecommendations();

        // Then
        verify(userService, times(1)).getAllUserIds();
        verify(redisLetterService, times(2)).updateRecommendationsFromTemp(anyLong());
        verify(notificationService, times(1)).sendKeywordNotifications(anyList());
    }
}
