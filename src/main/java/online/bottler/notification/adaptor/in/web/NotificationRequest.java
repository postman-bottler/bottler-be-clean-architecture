package online.bottler.notification.adaptor.in.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotEmpty(message = "알림 유형은 필수입니다.")
        String notificationType,

        @NotNull(message = "알림 대상은 필수입니다.")
        Long receiver,

        Long letterId,

        String label
) {

}
