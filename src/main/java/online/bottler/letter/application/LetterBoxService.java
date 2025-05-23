package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.letter.application.command.LetterBoxCommand;
import online.bottler.letter.adapter.in.web.request.PageRequest;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.application.port.out.LetterBoxRepository;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.exception.InvalidLetterRequestException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterBoxService {

    private final LetterBoxRepository letterBoxRepository;

    @Transactional
    public void saveLetter(LetterBoxCommand letterBoxCommand) {
        letterBoxRepository.save(letterBoxCommand.toDomain());
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> findAllLetterSummaries(PageRequest pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.NONE);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> findSentLetterSummaries(PageRequest pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.SEND);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponse> findReceivedLetterSummaries(PageRequest pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.RECEIVE);
    }

    @Transactional(readOnly = true)
    public List<Long> findReceivedLetterIdsByUserId(Long userId) {
        return letterBoxRepository.findReceivedLetterIdsByUserId(userId);
    }

    @Transactional
    public void deleteByLetterIdsAndType(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        log.info("편지 삭제 요청: letterIds={}, 편지 타입={}, 보관 타입={}", letterIds, letterType, boxType);

        letterBoxRepository.deleteByCondition(letterIds, letterType, boxType);

        log.info("편지 삭제 완료: 삭제된 개수={}", letterIds.size());
    }

    @Transactional
    public void deleteByLetterIdsAndTypeForUser(List<Long> letterIds, LetterType letterType, BoxType boxType,
                                                Long userId) {
        validateLetterIds(letterIds);
        letterBoxRepository.deleteByConditionAndUserId(letterIds, letterType, boxType, userId);

        log.info("사용자가 요청한 편지 삭제 완료: userId={}, 삭제된 개수={}", userId, letterIds.size());
    }

    @Transactional
    public void deleteAllByBoxTypeForUser(Long userId, BoxType boxType) {
        letterBoxRepository.deleteAllByBoxTypeForUser(userId, boxType);
    }

    public void validateLetterInUserBox(Long userId, Long letterId) {
        log.debug("편지 보관함 권한 검증 요청: userId={}, letterId={}", userId, letterId);

        boolean isLetterInUserBox = letterBoxRepository.existsByUserIdAndLetterId(userId, letterId);
        if (!isLetterInUserBox) {
            throw new UnauthorizedLetterAccessException();
        }
    }

    private Page<LetterSummaryResponse> findLetters(Long userId, Pageable pageable, BoxType boxType) {
        return letterBoxRepository.findLetters(userId, pageable, boxType);
    }

    private void validateLetterIds(List<Long> letterIds) {
        if (letterIds == null || letterIds.isEmpty()) {
            throw new InvalidLetterRequestException("삭제할 편지 ID 목록이 비어 있습니다.");
        }
    }
}
