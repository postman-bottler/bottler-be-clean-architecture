package online.bottler.mapletter.application.port.in;

import java.math.BigDecimal;
import java.util.List;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.command.CreatePublicMapLetterCommand;
import online.bottler.mapletter.application.command.CreateReplyMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteArchivedLettersCommand;
import online.bottler.mapletter.application.command.DeleteMapLettersCommand;
import online.bottler.mapletter.application.command.DeleteReplyMapLettersCommand;
import online.bottler.mapletter.application.response.CheckReplyMapLetterResponse;
import online.bottler.mapletter.application.response.FindAllArchiveLettersResponse;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllReceivedReplyLetterResponse;
import online.bottler.mapletter.application.response.FindAllReplyMapLettersResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentReplyMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.application.response.OneReplyLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterArchive;
import online.bottler.mapletter.domain.ReplyMapLetter;
import org.springframework.data.domain.Page;
import online.bottler.notification.application.dto.request.NotificationLabelRequestDTO;

public interface MapLetterUseCase {

    MapLetter createPublicMapLetter(CreatePublicMapLetterCommand createPublicMapLetterCommand,
                                    Long userId);

    MapLetter createTargetMapLetter(CreateTargetMapLetterCommand createTargetMapLetterCommand,
                                    Long userId);

    OneLetterResponse findOneMapLetter(Long letterId, Long userId, BigDecimal latitude,
                                       BigDecimal longitude);

    void deleteMapLetter(List<Long> letters, Long userId);

    Page<FindMapLetterResponse> findSentMapLetters(int page, int size, Long userId);

    Page<FindReceivedMapLetterResponse> findReceivedMapLetters(int page, int size, Long userId);

    List<FindNearbyLettersResponse> findNearByMapLetters(BigDecimal latitude, BigDecimal longitude,
                                                         Long userId);

    ReplyMapLetter createReplyMapLetter(CreateReplyMapLetterCommand createReplyMapLetterCommand, Long userId);

    Page<FindAllReplyMapLettersResponse> findAllReplyMapLetter(int page, int size, Long letterId,
                                                               Long userId);

    OneReplyLetterResponse findOneReplyMapLetter(Long letterId, Long userId);

    MapLetterArchive mapLetterArchive(Long letterId, Long userId);

    Page<FindAllArchiveLettersResponse> findArchiveLetters(int page, int size, Long userId);

    void deleteArchivedLetter(DeleteArchivedLettersCommand deleteArchivedLettersCommand, Long userId);

    CheckReplyMapLetterResponse checkReplyMapLetter(Long letterId, Long userId);

    Long letterBlock(BlockMapLetterType type, Long letterId);

    void deleteReplyMapLetter(DeleteReplyMapLettersCommand deleteReplyMapLettersCommand, Long userId);

    OneLetterResponse findArchiveOneLetter(Long letterId, Long userId);

    Page<FindAllSentReplyMapLetterResponse> findAllSentReplyMapLetter(int page, int size, Long userId);

    Page<FindAllSentMapLetterResponse> findAllSentMapLetter(int page, int size, Long userId);

    Page<FindAllReceivedReplyLetterResponse> findAllReceivedReplyLetter(int page, int size, Long userId);

    Page<FindAllReceivedLetterResponse> findAllReceivedLetter(int page, int size, Long userId);

    List<FindNearbyLettersResponse> guestFindNearByMapLetters(BigDecimal latitude, BigDecimal longitude);

    OneLetterResponse guestFindOneMapLetter(Long letterId, BigDecimal latitude, BigDecimal longitude);

    List<NotificationLabelRequestDTO> getLabels(List<Long> ids);

    boolean isArchived(Long letterId, Long userId);

    void deleteAllMapLetters(String type, Long userId);

    void deleteSentMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId);

    void deleteReceivedMapLetters(DeleteMapLettersCommand deleteMapLettersCommand, Long userId);
}
