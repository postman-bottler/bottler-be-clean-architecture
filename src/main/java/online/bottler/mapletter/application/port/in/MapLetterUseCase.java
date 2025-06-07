package online.bottler.mapletter.application.port.in;

import java.util.List;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.command.CreatePublicMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.domain.MapLetter;
import org.springframework.data.domain.Page;

public interface MapLetterUseCase {
    MapLetter createPublicMapLetter(CreatePublicMapLetterCommand createPublicMapLetterCommand, Long userId);

    MapLetter createTargetMapLetter(CreateTargetMapLetterCommand createTargetMapLetterCommand, Long userId,
                                    Long targetUserId);

    Page<FindSentMapLetter> findSentLetters(int page, int size, Long userId);

    Page<FindReceivedMapLetterDTO> findReceivedMapLetters(int page, int size, Long userId);

    void deleteMapLetter(List<Long> letters, Long userId);

    void deleteAllMapLetters(String type, Long userId);

    void deleteSentMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId);

    void deleteReceivedMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId);

    Long letterBlock(BlockMapLetterType type, Long letterId);

    MapLetter findById(Long id);

    void targetUserUpdateRead(Long userId, MapLetter mapLetter);

    Page<MapLetter> findActiveByCreateUserId(int page, int size, Long userId);

    Page<MapLetter> findActiveLetter(int page, int size, Long userId);
}
