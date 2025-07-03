package online.bottler.user.application;

import lombok.RequiredArgsConstructor;
import online.bottler.notification.application.port.NotificationUseCase;
import online.bottler.slack.application.port.in.SlackUseCase;
import online.bottler.slack.domain.SlackConstant;
import online.bottler.user.application.port.in.BanUseCase;
import online.bottler.user.application.port.in.UserUseCase;
import online.bottler.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserUseCase userUseCase;
    private final SlackUseCase slackUseCase;
    private final BanUseCase banUseCase;
    private final NotificationUseCase notificationUseCase;

    @Transactional
    public void updateWarningCount(Long userId) {
        User user = userUseCase.updateUserWarningCountByUserId(userId);

        slackUseCase.sendSlackMessage(SlackConstant.WARNING, user.getUserId());

        if (user.checkBan()) {
            banUseCase.banUser(user);
            slackUseCase.sendSlackMessage(SlackConstant.BAN, userId);
            notificationUseCase.sendBanNotification(userId);
        }

        userUseCase.updateWarningCount(user);
    }

}
