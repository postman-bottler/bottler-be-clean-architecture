package online.bottler.mapletter.application.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import online.bottler.mapletter.domain.MapLetterArchive;
import online.bottler.mapletter.application.dto.FindAllArchiveLettersDTO;

public interface MapLetterArchivePersistencePort {
    MapLetterArchive save(MapLetterArchive archive);

    Page<FindAllArchiveLettersDTO> findAllById(Long userId, Pageable pageable);

    MapLetterArchive findById(Long letterId);

    boolean findByLetterIdAndUserId(Long letterId, Long userId);

    List<MapLetterArchive> findAllById(List<Long> letterIds);

    void deleteAllByIdInBatch(List<Long> letterIds);
}
