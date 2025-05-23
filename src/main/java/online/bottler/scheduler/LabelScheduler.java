package online.bottler.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.label.application.command.LabelCommand;
import online.bottler.label.application.port.out.LabelPersistencePort;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.label.domain.Label;
import online.bottler.label.domain.LabelType;

@Component
@RequiredArgsConstructor
@Slf4j
public class LabelScheduler {
    private final LabelPersistencePort labelPersistencePort;
    private final TaskScheduler taskScheduler;

    private final Map<LocalDateTime, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Transactional
    public void scheduleUpdateFirstComeLabel(LabelCommand labelCommand) {
        LocalDateTime scheduledTime = labelCommand.scheduledDateTime();
        List<Long> labelIds = labelCommand.labelIds();

        log.info("{}에 선착순 라벨 변경 스케줄 등록", scheduledTime);

        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            // 1. 현재 FIRST_COME 라벨 조회
            List<Label> firstComeLabels = labelPersistencePort.findByLabelType(LabelType.FIRST_COME);
            Set<Long> firstComeLabelIds = firstComeLabels.stream()
                    .map(Label::getLabelId)
                    .collect(Collectors.toSet());

            // 2. 요청된 라벨 중 현재 FIRST_COME이 아닌 것 -> FIRST_COME으로 변경
            for (Long labelId : labelIds) {
                if (!firstComeLabelIds.contains(labelId)) {
                    Label label = labelPersistencePort.findLabelByLabelId(labelId);
                    label.updateFirstComeLabel();
                    labelPersistencePort.save(label);
                    log.info("라벨 {}을 FIRST_COME으로 변경", labelId);
                }
            }

            // 3. 기존 FIRST_COME 라벨 중 요청되지 않은 것 -> GENERAL로 변경
            for (Label label : firstComeLabels) {
                if (!labelIds.contains(label.getLabelId())) {
                    label.updateGeneralLabel();
                    labelPersistencePort.save(label);
                    log.info("라벨 {}을 GENERAL로 변경", label.getLabelId());
                }
            }

            scheduledTasks.remove(scheduledTime);
        }, scheduledTime.atZone(ZoneId.systemDefault()).toInstant());

        // 동일 시간 스케줄이 이미 있으면 취소
        if (scheduledTasks.containsKey(scheduledTime)) {
            scheduledTasks.get(scheduledTime).cancel(false);
            log.info("기존 스케줄 취소: {}", scheduledTime);
        }

        scheduledTasks.put(scheduledTime, scheduledTask);
    }
}
