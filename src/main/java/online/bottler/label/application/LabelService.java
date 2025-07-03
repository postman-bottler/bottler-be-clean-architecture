package online.bottler.label.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.global.exception.ApplicationException;
import online.bottler.label.application.port.out.LabelPersistencePort;
import online.bottler.label.domain.Label;
import online.bottler.label.domain.LabelType;
import online.bottler.label.application.command.LabelCommand;
import online.bottler.label.application.port.in.LabelUseCase;
import online.bottler.label.application.response.LabelResponse;
import online.bottler.scheduler.LabelScheduler;
import online.bottler.user.application.port.in.UserUseCase;
import online.bottler.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LabelService implements LabelUseCase {
    private final LabelPersistencePort labelPersistencePort;
    private final UserUseCase userUseCase;
    private final LabelScheduler labelScheduler;

    @Transactional
    public void createLabel(String imageUrl, int limitCount) {
        labelPersistencePort.save(Label.createLabel(imageUrl, limitCount));
    }

    @Transactional
    public List<LabelResponse> findAllLabels() {
        List<Label> labels = labelPersistencePort.findAllLabels();
        return labels.stream().map(Label::toLabelResponse).toList();
    }

    @Transactional
    public List<LabelResponse> findUserLabels(Long userId) {
        List<Label> labels = labelPersistencePort.findLabelsByUser(userId);
        return labels.stream().map(Label::toLabelResponse).toList();
    }

    @Transactional
    public LabelResponse createFirstComeFirstServedLabel(Long userId) {
        User user = userUseCase.findById(userId);

        List<Label> firstComeLabels = labelPersistencePort.findByLabelType(LabelType.FIRST_COME);

        for (Label label : firstComeLabels) {
            boolean hasLabel = labelPersistencePort.existsUserLabelByUserAndLabel(user, label);
            if (!hasLabel && label.isOwnedCountValid()) {
                labelPersistencePort.updateOwnedCount(label);
                labelPersistencePort.createUserLabel(user, label);
                return label.toLabelResponse();
            }
        }

        throw new ApplicationException("모든 선착순 뽑기 라벨이 마감되었습니다.");
    }

    @Transactional
    public List<LabelResponse> findFirstComeLabels() {
        List<Label> labels = labelPersistencePort.findFirstComeLabels();
        return labels.stream().map(Label::toLabelResponse).toList();
    }

    @Transactional
    public void updateFirstComeLabel(LabelCommand labelCommand) {
        labelScheduler.scheduleUpdateFirstComeLabel(labelCommand);
    }

    @Override
    public Label findLabelByLabelId(Long labelId) {
        return labelPersistencePort.findLabelByLabelId(labelId);
    }

    @Override
    public void updateOwnedCount(Label label) {
        labelPersistencePort.updateOwnedCount(label);
    }

    @Override
    public void createUserLabel(User user, Label label) {
        labelPersistencePort.createUserLabel(user, label);
    }
}
