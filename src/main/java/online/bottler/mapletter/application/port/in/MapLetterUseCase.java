package online.bottler.mapletter.application.port.in;

import java.util.List;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.command.CreatePublicMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import org.springframework.data.domain.Page;
import online.bottler.notification.application.dto.request.NotificationLabelRequestDTO;

public interface MapLetterUseCase {
    MapLetter createPublicMapLetter(CreatePublicMapLetterCommand createPublicMapLetterCommand, Long userId);

    MapLetter createTargetMapLetter(CreateTargetMapLetterCommand createTargetMapLetterCommand, Long userId);

    Page<FindMapLetterResponse> findSentMapLetters(int page, int size, Long userId);

    Page<FindReceivedMapLetterResponse> findReceivedMapLetters(int page, int size, Long userId);

    Page<FindAllSentMapLetterResponse> findAllSentMapLetter(int page, int size, Long userId);

    Page<FindAllReceivedLetterResponse> findAllReceivedLetter(int page, int size, Long userId);

    void deleteMapLetter(List<Long> letters, Long userId);

    void deleteAllMapLetters(String type, Long userId);

    void deleteSentMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId);

    void deleteReceivedMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId);

    Long letterBlock(BlockMapLetterType type, Long letterId);

    List<NotificationLabelRequestDTO> getLabels(List<Long> ids);
}
