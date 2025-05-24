package online.bottler.label.application.port.in;

import java.util.List;
import online.bottler.label.application.command.LabelCommand;
import online.bottler.label.application.response.LabelResponse;

public interface LabelUseCase {
    void createLabel(String imageUrl, int limitCount);

    List<LabelResponse> findAllLabels();

    List<LabelResponse> findUserLabels(Long userId);

    LabelResponse createFirstComeFirstServedLabel(Long userId);

    List<LabelResponse> findFirstComeLabels();

    void updateFirstComeLabel(LabelCommand labelCommand);
}
