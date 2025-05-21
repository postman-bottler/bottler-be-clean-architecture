package online.bottler.letter.application;

import static online.bottler.notification.domain.NotificationType.KEYWORD_REPLY;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.letter.application.dto.LetterBoxDTO;
import online.bottler.letter.application.dto.ReceiverDTO;
import online.bottler.letter.application.dto.request.PageRequestDTO;
import online.bottler.letter.application.dto.request.ReplyLetterRequestDTO;
import online.bottler.letter.application.dto.response.ReplyLetterDetailResponseDTO;
import online.bottler.letter.application.dto.response.ReplyLetterResponseDTO;
import online.bottler.letter.application.dto.response.ReplyLetterSummaryResponseDTO;
import online.bottler.letter.application.repository.LetterBoxRepository;
import online.bottler.letter.application.repository.LetterRepository;
import online.bottler.letter.application.repository.ReplyLetterRepository;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterType;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.DuplicateReplyLetterException;
import online.bottler.letter.exception.InvalidLetterRequestException;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import online.bottler.letter.exception.LetterNotFoundException;
import online.bottler.notification.application.NotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyLetterService {

    private final ReplyLetterRepository replyLetterRepository;
    private final LetterRepository letterRepository;
    private final LetterBoxRepository letterBoxRepository;
    private final NotificationService notificationService;
    private final RedisLetterService redisLetterService;

    @Transactional
    public ReplyLetterResponseDTO createReplyLetter(Long letterId, ReplyLetterRequestDTO requestDTO, Long senderId) {
        log.info("답장 생성 요청: senderId={}, letterId={}", senderId, letterId);

        if (checkIsReplied(letterId, senderId)) {
            throw new DuplicateReplyLetterException();
        }

        ReplyLetter replyLetter = saveReplyLetter(letterId, requestDTO, senderId);
        handleReplyPostProcessing(replyLetter, requestDTO.label());

        return ReplyLetterResponseDTO.from(replyLetter);
    }

    @Transactional(readOnly = true)
    public Page<ReplyLetterSummaryResponseDTO> findReplyLetterSummaries(Long letterId, PageRequestDTO pageRequestDTO,
                                                                        Long receiverId) {
        return replyLetterRepository.findAllByLetterIdAndReceiverId(letterId, receiverId, pageRequestDTO.toPageable())
                .map(ReplyLetterSummaryResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public ReplyLetterDetailResponseDTO findReplyLetterDetail(Long replyLetterId, Long userId) {
        boolean isReplied = checkIsReplied(replyLetterId, userId);
        ReplyLetter replyLetter = findReplyLetter(replyLetterId);
        return ReplyLetterDetailResponseDTO.from(replyLetter, isReplied);
    }

    @Transactional
    public void softDeleteReplyLetters(List<Long> replyLetterIds, Long senderId) {
        log.info("답장 삭제 요청: userId={}, replyLetterIds={}", senderId, replyLetterIds);

        if (replyLetterIds == null || replyLetterIds.isEmpty()) {
            throw new InvalidLetterRequestException("삭제할 답장 편지 ID 목록이 비어 있습니다.");
        }

        List<ReplyLetter> replyLetters = replyLetterRepository.findAllByIds(replyLetterIds);

        if (replyLetters.stream().anyMatch(replyLetter -> !replyLetter.getSenderId().equals(senderId))) {
            throw new LetterAuthorMismatchException();
        }

        replyLetters.forEach(
                replyLetter -> redisLetterService.deleteRecentReply(replyLetter.getReceiverId(), replyLetter.getId(),
                        replyLetter.getLabel()));

        replyLetterRepository.softDeleteByIds(replyLetterIds);
    }

    @Transactional
    public Long softBlockLetter(Long replyLetterId) {
        log.info("답장 차단 요청: replyLetterId={}", replyLetterId);

        ReplyLetter replyLetter = findReplyLetter(replyLetterId);
        replyLetterRepository.softBlockById(replyLetterId);

        return replyLetter.getSenderId();
    }

    public boolean checkIsReplied(Long letterId, Long senderId) {
        return replyLetterRepository.existsByLetterIdAndSenderId(letterId, senderId);
    }

    private ReplyLetter saveReplyLetter(Long letterId, ReplyLetterRequestDTO requestDTO, Long senderId) {
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.LETTER));
        ReceiverDTO receiverInfo = ReceiverDTO.from(letter);

        String title = formatReplyTitle(receiverInfo.title());

        return replyLetterRepository.save(requestDTO.toDomain(title, letterId, receiverInfo.receiverId(), senderId));
    }

    private String formatReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    private void handleReplyPostProcessing(ReplyLetter replyLetter, String labelUrl) {
        saveReplyLetterToBox(replyLetter);
        redisLetterService.saveReplyToRedis(replyLetter.getId(), labelUrl, replyLetter.getReceiverId());
        sendReplyNotification(replyLetter);
    }

    private void saveReplyLetterToBox(ReplyLetter replyLetter) {
        saveLetterToBox(replyLetter.getSenderId(), replyLetter, BoxType.SEND);
        saveLetterToBox(replyLetter.getReceiverId(), replyLetter, BoxType.RECEIVE);
    }

    private void saveLetterToBox(Long userId, ReplyLetter replyLetter, BoxType boxType) {
        letterBoxRepository.save(LetterBoxDTO.of(userId, replyLetter.getId(), LetterType.REPLY_LETTER, boxType,
                replyLetter.getCreatedAt()).toDomain());
    }

    private void sendReplyNotification(ReplyLetter replyLetter) {
        log.info("답장 알림 전송 요청: receiverId={}, replyLetterId={}", replyLetter.getReceiverId(), replyLetter.getId());

        notificationService.sendLetterNotification(KEYWORD_REPLY, replyLetter.getReceiverId(), replyLetter.getId(),
                replyLetter.getLabel());
    }

    private ReplyLetter findReplyLetter(Long replyLetterId) {
        return replyLetterRepository.findById(replyLetterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.REPLY_LETTER));
    }

    public List<Long> findIdsBySenderId(Long senderId) {
        return replyLetterRepository.findAllBySenderId(senderId).stream().map(ReplyLetter::getId).toList();
    }
}
