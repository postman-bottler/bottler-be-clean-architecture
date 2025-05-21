package online.bottler.label.application.repository;

import java.util.List;
import online.bottler.label.domain.Label;
import online.bottler.label.domain.LabelType;
import online.bottler.user.domain.User;

public interface LabelRepository {
    void save(Label label);

    List<Label> findAllLabels();

    List<Label> findLabelsByUser(Long userId);

    Label findLabelByLabelId(Long labelId);

    void updateOwnedCount(Label label);

    void createUserLabel(User user, Label label);

    List<Label> findFirstComeLabels();

    List<Label> findByLabelType(LabelType labelType);

    boolean existsUserLabelByUserAndLabel(User user, Label label);
}
