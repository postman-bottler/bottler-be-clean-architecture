package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterSummary;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;

public interface LetterBoxUseCase {

    void archiveLetter(Letter letter);

    void archiveLetter(ReplyLetter replyLetter);

    void archiveLetters(List<Long> letterId, Long userId);

    Page<LetterSummary> getLetterBoxSummaries(Long userId, BoxType boxType, CommonPageCommand commonPageCommand);

    void removeLetterFromBox(Long letterId, LetterBoxType letterBoxType);

    void removeLettersFromBox(List<Long> letterIds, LetterBoxType letterBoxType);

    void removeLettersFromBox(Long userId, LetterBoxType letterBoxType);

    void removeLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType);

    boolean isAccessDenied(Long letterId, Long userId);
}
