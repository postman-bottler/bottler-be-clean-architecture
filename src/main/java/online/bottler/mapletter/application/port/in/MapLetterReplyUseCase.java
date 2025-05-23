package online.bottler.mapletter.application.port.in;

import online.bottler.mapletter.application.command.CreateReplyMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteReplyMapLettersCommand;
import online.bottler.mapletter.application.response.CheckReplyMapLetterResponse;
import online.bottler.mapletter.application.response.FindAllReceivedReplyLetterResponse;
import online.bottler.mapletter.application.response.FindAllReplyMapLettersResponse;
import online.bottler.mapletter.application.response.FindAllSentReplyMapLetterResponse;
import online.bottler.mapletter.application.response.OneReplyLetterResponse;
import online.bottler.mapletter.domain.ReplyMapLetter;
import org.springframework.data.domain.Page;

public interface MapLetterReplyUseCase {
    ReplyMapLetter createReplyMapLetter(CreateReplyMapLetterCommand createReplyMapLetterCommand, Long userId);

    Page<FindAllReplyMapLettersResponse> findAllReplyMapLetter(int page, int size, Long letterId, Long userId);

    OneReplyLetterResponse findOneReplyMapLetter(Long letterId, Long userId);

    CheckReplyMapLetterResponse checkReplyMapLetter(Long letterId, Long userId);

    void deleteReplyMapLetter(DeleteReplyMapLettersCommand deleteReplyMapLettersCommand, Long userId);

    Page<FindAllSentReplyMapLetterResponse> findAllSentReplyMapLetter(int page, int size, Long userId);

    Page<FindAllReceivedReplyLetterResponse> findAllReceivedReplyLetter(int page, int size, Long userId);
}
