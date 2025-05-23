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

    Page<FindAllReplyMapLettersResponse> findAllReplyMapLetters(int page, int size, Long letterId, Long userId);

    OneReplyLetterResponse findReplyMapLetter(Long letterId, Long userId);

    CheckReplyMapLetterResponse hasReplyForMapLetter(Long letterId, Long userId);

    void deleteReplyMapLetter(DeleteReplyMapLettersCommand deleteReplyMapLettersCommand, Long userId);

    Page<FindAllSentReplyMapLetterResponse> findAllSentReplyMapLetters(int page, int size, Long userId);

    Page<FindAllReceivedReplyLetterResponse> findAllReceivedReplyMapLetters(int page, int size, Long userId);
}
