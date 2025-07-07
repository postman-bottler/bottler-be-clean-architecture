package online.bottler.letter.application.facade;

import static online.bottler.letter.domain.LetterType.REPLY_LETTER;
import static online.bottler.notification.domain.NotificationType.KEYWORD_REPLY;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.ReplyLetterCommand;
import online.bottler.letter.application.command.ReplyLetterDeleteCommand;
import online.bottler.letter.application.command.ReplyLetterSummariesQuery;
import online.bottler.letter.application.port.in.LetterBoxUseCase;
import online.bottler.letter.application.port.in.RecentReplyForLetterUseCase;
import online.bottler.letter.application.port.in.ReplyLetterUseCase;
import online.bottler.letter.application.response.ReplyLetterDetailResponse;
import online.bottler.letter.application.response.ReplyLetterResponse;
import online.bottler.letter.application.response.ReplyLetterSummaryResponse;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.ReplyLetter;
import online.bottler.letter.exception.UnauthorizedLetterAccessException;
import online.bottler.notification.application.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReplyLetterFacade {

    private final ReplyLetterUseCase replyLetterUseCase;
    private final NotificationService notificationService;
    private final LetterBoxUseCase letterBoxUseCase;
    private final RecentReplyForLetterUseCase recentReplyForLetterUseCase;

    @Transactional
    public ReplyLetterResponse write(ReplyLetterCommand replyLetterCommand) {
        ReplyLetter replyLetter = replyLetterUseCase.write(replyLetterCommand);

        letterBoxUseCase.archiveLetter(replyLetter);

        recentReplyForLetterUseCase.push(replyLetter.getId(), replyLetter.getLabel(), replyLetter.getReceiverId());

        notificationService.sendLetterNotification(KEYWORD_REPLY, replyLetter.getReceiverId(), replyLetter.getId(),
                replyLetter.getLabel());

        return ReplyLetterResponse.from(replyLetter);
    }

    @Transactional(readOnly = true)
    public Page<ReplyLetterSummaryResponse> getSummaries(ReplyLetterSummariesQuery replyLetterSummariesQuery) {
        validateUserAccess(replyLetterSummariesQuery.userId(), replyLetterSummariesQuery.letterId());
        return replyLetterUseCase.getSummaries(replyLetterSummariesQuery).map(ReplyLetterSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public ReplyLetterDetailResponse getDetail(Long id, Long userId) {
        validateUserAccess(userId, id);
        ReplyLetter replyLetter = replyLetterUseCase.get(id);
        boolean isReplied = replyLetterUseCase.isReplied(userId, id);
        return ReplyLetterDetailResponse.from(replyLetter, isReplied);
    }

    @Transactional
    public void softDelete(ReplyLetterDeleteCommand replyLetterDeleteCommand) {
        ReplyLetter replyLetter = replyLetterUseCase.softDelete(replyLetterDeleteCommand);
        letterBoxUseCase.removeLetterFromBox(replyLetterDeleteCommand.id(), LetterBoxType.of(REPLY_LETTER,
                replyLetterDeleteCommand.boxType()));
        recentReplyForLetterUseCase.delete(replyLetter.getReceiverId(), replyLetter.getId(), replyLetter.getLabel());
    }

    private void validateUserAccess(Long userId, Long letterId) {
        if (letterBoxUseCase.isAccessDenied(userId, letterId)) {
            throw new UnauthorizedLetterAccessException();
        }
    }
}
