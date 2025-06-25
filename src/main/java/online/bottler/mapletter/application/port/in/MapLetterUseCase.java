package online.bottler.mapletter.application.port.in;

import java.util.List;
import java.util.Map;
import java.util.Set;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.command.CreatePublicMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
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

    MapLetter findArchiveOneLetterById(Long letterId, Long userId);

    MapLetter findPublicMapLetterById(Long letterId);

    Set<Long> extractTargetUserIdsBySentMapLetters(Page<FindSentMapLetter> sentLetters);

    Page<FindMapLetterResponse> convertToFindMapLetterResponse(Page<FindSentMapLetter> sentLetters,
                                                               Map<Long, String> nicknamesByIds);

    Set<Long> extractSentUserIdsByReceivedMapLetters(Page<FindReceivedMapLetterDTO> letters);

    Page<FindReceivedMapLetterResponse> convertToFindReceivedMapLetterResponse(Page<FindReceivedMapLetterDTO> letters,
                                                                               Map<Long, String> nicknamesByIds,
                                                                               Map<Long, String> profileImageUrlsByIds);

    Set<Long> extractTargetUserIdsByMapLetters(Page<MapLetter> letters);

    Page<FindAllSentMapLetterResponse> convertToFindAllSentMapLetterResponse(Page<MapLetter> letters,
                                                                             Map<Long, String> nicknamesByIds);

    Set<Long> extractCreateUserIdsByMapLetters(Page<MapLetter> letters);

    Page<FindAllReceivedLetterResponse> convertToFindAllReceivedLetterResponse(Page<MapLetter> letters,
                                                                               Map<Long, String> nicknamesByIds,
                                                                               Map<Long, String> profileImageUrlsByIds);
}
