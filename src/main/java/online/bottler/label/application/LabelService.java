package online.bottler.label.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.label.application.dto.LabelRequestDTO;
import online.bottler.label.application.repository.LabelRepository;
import online.bottler.label.domain.Label;
import online.bottler.label.domain.LabelType;
import online.bottler.label.application.dto.LabelResponseDTO;
import online.bottler.label.exception.FirstComeFirstServedLabelException;
import online.bottler.scheduler.LabelScheduler;
import online.bottler.user.domain.User;
import online.bottler.user.application.UserService;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final UserService userService;
    private final LabelScheduler labelScheduler;

    @Transactional
    public void createLabel(String imageUrl, int limitCount) {
        labelRepository.save(Label.createLabel(imageUrl, limitCount));
    }

    @Transactional
    public List<LabelResponseDTO> findAllLabels() {
        List<Label> labels = labelRepository.findAllLabels();
        return labels.stream().map(Label::toLabelResponseDTO).toList();
    }

    @Transactional
    public List<LabelResponseDTO> findUserLabels(Long userId) {
        List<Label> labels = labelRepository.findLabelsByUser(userId);
        return labels.stream().map(Label::toLabelResponseDTO).toList();
    }

    @Transactional
    public LabelResponseDTO createFirstComeFirstServedLabel(Long userId) {
        User user = userService.findById(userId);

        List<Label> firstComeLabels = labelRepository.findByLabelType(LabelType.FIRST_COME);

        for (Label label : firstComeLabels) {
            boolean hasLabel = labelRepository.existsUserLabelByUserAndLabel(user, label);
            if (!hasLabel && label.isOwnedCountValid()) {
                labelRepository.updateOwnedCount(label);
                labelRepository.createUserLabel(user, label);
                return label.toLabelResponseDTO();
            }
        }

        throw new FirstComeFirstServedLabelException("모든 선착순 뽑기 라벨이 마감되었습니다.");
    }

    @Transactional
    public List<LabelResponseDTO> findFirstComeLabels() {
        List<Label> labels = labelRepository.findFirstComeLabels();
        return labels.stream().map(Label::toLabelResponseDTO).toList();
    }

    @Transactional
    public void updateFirstComeLabel(LabelRequestDTO labelRequestDTO) {
        labelScheduler.scheduleUpdateFirstComeLabel(labelRequestDTO);
    }
}
