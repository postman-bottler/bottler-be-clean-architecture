package online.bottler.mapletter.application.port.in;

import online.bottler.mapletter.application.command.DeleteArchivedLettersCommand;
import online.bottler.mapletter.application.response.FindAllArchiveLettersResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.domain.MapLetterArchive;
import org.springframework.data.domain.Page;

public interface MapLetterArchiveUseCase {
    MapLetterArchive mapLetterArchive(Long letterId, Long userId);

    Page<FindAllArchiveLettersResponse> findArchiveLetters(int page, int size, Long userId);

    void deleteArchivedLetter(DeleteArchivedLettersCommand deleteArchivedLettersCommand, Long userId);

    OneLetterResponse findArchiveOneLetter(Long letterId, Long userId);

    boolean isArchived(Long letterId, Long userId);
}
