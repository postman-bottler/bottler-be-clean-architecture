package online.bottler.letter.application.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBox;
import online.bottler.letter.domain.LetterType;

public interface LetterBoxRepository {
    void save(LetterBox letterBox);

    Page<LetterSummaryResponse> findLetters(Long userId, Pageable pageable, BoxType boxType);

    List<Long> findReceivedLetterIdsByUserId(Long userId);

    void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType);

    void deleteByConditionAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId);

    void deleteAllByBoxTypeForUser(Long userId, BoxType boxType);

    boolean existsByUserIdAndLetterId(Long userId, Long letterId);
}
