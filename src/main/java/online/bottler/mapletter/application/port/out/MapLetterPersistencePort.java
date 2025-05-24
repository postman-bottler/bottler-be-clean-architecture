package online.bottler.mapletter.application.port.out;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;

public interface MapLetterPersistencePort {
    MapLetter save(MapLetter mapLetter);

    MapLetter findById(Long id);

    void softDelete(Long letterId);

    Page<MapLetter> findActiveByCreateUserId(Long userId, Pageable pageable);

    Page<MapLetter> findActiveByTargetUserId(Long userId, Pageable pageable);

    List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId);

    MapLetter findSourceMapLetterById(Long sourceMapLetterId);

    void letterBlock(Long letterId);

    Double findDistanceByLatitudeAndLongitudeAndLetterId(BigDecimal latitude, BigDecimal longitude, Long letterId);

    Page<FindSentMapLetter> findSentLettersByUserId(Long userId, Pageable createdAt);

    Page<FindReceivedMapLetterDTO> findActiveReceivedMapLettersByUserId(Long userId, PageRequest pageRequest);

    List<MapLetterAndDistance> guestFindLettersByUserLocation(BigDecimal latitude, BigDecimal longitude);

    List<MapLetter> findAllByIds(List<Long> ids);

    void updateRead(MapLetter mapLetter);

    void softDeleteAllByCreateUserId(Long userId);

    void softDeleteForRecipient(Long letterId);

    void softDeleteAllForRecipient(Long userId);

    void softDeleteAll(List<MapLetter> mapLetters);
}
