package online.bottler.label.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.global.exception.AdaptorException;
import online.bottler.label.domain.Label;
import online.bottler.label.domain.LabelType;
import online.bottler.label.infra.LabelJpaRepository;
import online.bottler.label.infra.UserLabelJpaRepository;
import online.bottler.label.infra.entity.LabelEntity;
import online.bottler.label.infra.entity.UserLabelEntity;
import online.bottler.label.application.port.out.LabelPersistencePort;
import online.bottler.user.domain.User;
import online.bottler.user.infra.UserJpaRepository;
import online.bottler.user.infra.entity.UserEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LabelPersistenceAdapter implements LabelPersistencePort {

    private final LabelJpaRepository labelJpaRepository;
    private final UserLabelJpaRepository userLabelJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(Label label) {
        try {
            labelJpaRepository.save(LabelEntity.from(label));
        } catch (DataIntegrityViolationException e) {
            throw new AdaptorException("이미 존재하는 라벨입니다.");
        }
    }

    @Override
    public List<Label> findAllLabels() {
        List<LabelEntity> labelEntities = labelJpaRepository.findAll();
        return LabelEntity.toLabels(labelEntities);
    }

    @Override
    public List<Label> findLabelsByUser(Long userId) {
        List<LabelEntity> labelEntities = userLabelJpaRepository.findLabelsByUserId(userId);

        if (labelEntities.isEmpty()) {
            throw new AdaptorException("유저 ID " + userId + " 에 해당하는 라벨이 존재하지 않습니다.");
        }

        return LabelEntity.toLabels(labelEntities);
    }

    @Override
    @Transactional
    public Label findLabelByLabelId(Long labelId) {
        LabelEntity labelEntity = labelJpaRepository.findByIdWithLock(labelId)
                .orElseThrow(() -> new AdaptorException("라벨 ID " + labelId + " 에 해당하는 라벨이 존재하지 않습니다."));
        return LabelEntity.toLabel(labelEntity);
    }

    @Override
    public void updateOwnedCount(Label label) {
        LabelEntity labelEntity = labelJpaRepository.findById(label.getLabelId())
                .orElseThrow();
        labelEntity.updateOwnedCount();
    }

    @Override
    public void createUserLabel(User user, Label label) {
        LabelEntity labelEntity = labelJpaRepository.findById(label.getLabelId())
                .orElseThrow();
        UserEntity userEntity = userJpaRepository.findById(user.getUserId())
                .orElseThrow();

        UserLabelEntity userLabelEntity = UserLabelEntity.from(userEntity, labelEntity);
        userLabelJpaRepository.save(userLabelEntity);
    }

    @Override
    public List<Label> findFirstComeLabels() {
        List<LabelEntity> labelEntities = userLabelJpaRepository.findFirstComeLabels(LabelType.FIRST_COME);
        return LabelEntity.toLabels(labelEntities);
    }

    @Override
    public List<Label> findByLabelType(LabelType labelType) {
        List<LabelEntity> labelEntities = labelJpaRepository.findByLabelType(labelType);
        return LabelEntity.toLabels(labelEntities);
    }

    @Override
    public boolean existsUserLabelByUserAndLabel(User user, Label label) {
        return userLabelJpaRepository.existsByUserUserIdAndLabelLabelId(user.getUserId(), label.getLabelId());
    }
}
