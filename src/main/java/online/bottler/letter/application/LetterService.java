package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.letter.application.port.out.LetterKeywordRepository;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.application.command.LetterBoxDTO;
import online.bottler.letter.adapter.in.web.request.LetterRequestDTO;
import online.bottler.letter.application.response.LetterDetailResponseDTO;
import online.bottler.letter.application.response.LetterRecommendSummaryResponseDTO;
import online.bottler.letter.application.response.LetterResponseDTO;
import online.bottler.letter.application.port.out.LetterBoxRepository;
import online.bottler.letter.application.port.out.LetterRepository;
import online.bottler.letter.application.port.out.ReplyLetterRepository;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import online.bottler.user.application.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final LetterKeywordRepository letterKeywordRepository;
    private final LetterBoxRepository letterBoxRepository;
    private final ReplyLetterRepository replyLetterRepository;
    private final UserRepository userRepository;
    private final RedisLetterService redisLetterService;

    @Transactional
    public LetterResponseDTO createLetter(LetterRequestDTO letterRequestDTO, Long userId) {
        Letter letter = letterRequestDTO.toDomain(userId);
        Letter savedLetter = letterRepository.save(letter);
        Long letterId = savedLetter.getId();

        List<String> keywords = letterRequestDTO.keywords();
        List<LetterKeyword> letterKeywords = keywords.stream().map(keyword -> LetterKeyword.create(letterId, keyword))
                .toList();
        letterKeywordRepository.saveAll(letterKeywords);

        letterBoxRepository.save(LetterBoxDTO.of(userId, letterId, LetterType.LETTER, BoxType.SEND,
                savedLetter.getCreatedAt()).toDomain());

        return LetterResponseDTO.from(letter, letterKeywords);
    }

    @Transactional(readOnly = true)
    public Letter findLetter(Long letterId) {
        return letterRepository.findById(letterId).orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));
    }

    @Transactional(readOnly = true)
    public LetterDetailResponseDTO findLetterDetail(Long userId, Long letterId) {
        boolean isLetterInUserBox = letterBoxRepository.existsByUserIdAndLetterId(userId, letterId);
        if (!isLetterInUserBox) {
            throw new UnauthorizedLetterAccessException();
        }

        boolean isReplied = replyLetterRepository.existsByLetterIdAndSenderId(letterId, userId);
        List<LetterKeyword> keywords = letterKeywordRepository.getKeywordsByLetterId(letterId);
        String profile = userRepository.findById(userId).getImageUrl();
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));

        return LetterDetailResponseDTO.from(letter, keywords, userId, profile, isReplied);
    }

    @Transactional(readOnly = true)
    public List<LetterRecommendSummaryResponseDTO> findRecommendHeaders(Long userId) {
        List<Long> letterIds = redisLetterService.fetchActiveRecommendations(userId);
        List<Letter> letters = letterRepository.findAllByIds(letterIds);
        return letters.stream().map(LetterRecommendSummaryResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<Long> findIdsByUserId(Long userId) {
        return letterRepository.findAllByUserId(userId).stream().map(Letter::getId).toList();
    }

    @Transactional
    public void softDeleteLetters(List<Long> letterIds, Long userId) {
        log.info("편지 삭제 요청: userId={}, letterIds={}", userId, letterIds);

        List<Letter> letters = letterRepository.findAllByIds(letterIds);
        validateLetterOwnerShip(userId, letters);

        letterRepository.softDeleteByIds(letterIds);
    }

    private void validateLetterOwnerShip(Long userId, List<Letter> letters) {
        if (letters.stream().anyMatch(letter -> !letter.getUserId().equals(userId))) {
            throw new LetterAuthorMismatchException();
        }
    }

    @Transactional
    public Long softBlockLetter(Long letterId) {
        log.info("편지 차단 요청: letterId={}", letterId);

        Letter letter = findLetter(letterId);
        letterRepository.softBlockById(letter.getId());

        return letter.getUserId();
    }

    @Transactional(readOnly = true)
    public boolean existsLetterById(Long letterId) {
        return letterRepository.existsById(letterId);
    }
}
