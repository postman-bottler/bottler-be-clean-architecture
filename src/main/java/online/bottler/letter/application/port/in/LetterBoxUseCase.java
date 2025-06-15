package online.bottler.letter.application.port.in;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.letter.application.command.CommonPageCommand;
import online.bottler.letter.application.command.LetterDeleteCommand;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterSummary;
import online.bottler.letter.domain.LetterType;

public interface LetterBoxUseCase {
    void save(List<Long> letterId, Long userId);

    void createForLetter(Long letterId, Long userId, LocalDateTime localDateTime);

    void createForReplyLetter(Long letterId, Long userId, Long receiverId, LocalDateTime createdAt);

    List<LetterSummary> getAllLetters(CommonPageCommand commonPageCommand, Long userId);

    List<LetterSummary> getReceivedLetters(CommonPageCommand commonPageCommand, Long userId);

    List<LetterSummary> getSentLetters(CommonPageCommand commonPageCommand, Long userId);

    void deleteLetter(Long letterId, LetterType letterType, BoxType boxType);

    void deleteLetters(List<LetterDeleteCommand> letterDeleteCommands, Long userId);

    void deleteAllLetters(Long userId);

    void deleteAllReceivedLetters(Long userId);

    void deleteAllSentLetters(Long userId);

    boolean isAccessDenied(Long letterId, Long userId);

    long countLetters(Long userId, BoxType boxType);

    void deleteAllLettersByBoxType(BoxType boxType, Long userId);
}
