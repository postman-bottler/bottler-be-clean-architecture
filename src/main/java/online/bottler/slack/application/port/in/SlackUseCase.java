package online.bottler.slack.application.port.in;

import online.bottler.slack.domain.SlackConstant;

public interface SlackUseCase {

    void sendSlackMessage(SlackConstant slackConstant, Long userId);

}
