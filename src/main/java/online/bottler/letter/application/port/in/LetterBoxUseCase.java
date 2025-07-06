package online.bottler.letter.application.port.in;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterSummary;
import org.springframework.data.domain.Page;

public interface LetterBoxUseCase {
    void save(List<Long> letterId, Long userId);

    void createForLetter(Long letterId, Long userId, LocalDateTime localDateTime);

    void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt);

    Page<LetterSummary> getAllLetters(CommonPageCommand commonPageCommand, Long userId);

    Page<LetterSummary> getReceivedLetters(CommonPageCommand commonPageCommand, Long userId);

    Page<LetterSummary> getSentLetters(CommonPageCommand commonPageCommand, Long userId);

    void removeLetterFromBox(Long letterId, LetterBoxType letterBoxType);

    void removeLettersFromBox(List<Long> letterIds, LetterBoxType letterBoxType);

    void removeLettersFromBox(Long userId, LetterBoxType letterBoxType);

    void removeLettersFromBox(Long userId, List<Long> letterIds, LetterBoxType letterBoxType);

    boolean isAccessDenied(Long letterId, Long userId);
}
