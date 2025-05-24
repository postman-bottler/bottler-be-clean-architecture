package online.bottler.label.adapter.in.web.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import online.bottler.label.application.command.LabelCommand;

public record LabelRequest(
        @NotEmpty(message = "라벨 ID 리스트는 비어 있을 수 없습니다.")
        List<Long> labelIds,

        @NotNull(message = "변경 예약 날짜와 시간은 필수 입력입니다.")
        @FutureOrPresent(message = "미래 또는 현재 시간으로 예약을 설정할 수 있습니다.")
        LocalDateTime scheduledDateTime
) {
        public LabelCommand toCommand() {
                return new LabelCommand(labelIds, scheduledDateTime);
        }
}
