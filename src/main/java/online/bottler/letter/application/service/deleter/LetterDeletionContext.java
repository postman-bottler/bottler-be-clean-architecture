package online.bottler.letter.application.service.deleter;

import online.bottler.letter.application.LetterKeywordService;
import online.bottler.letter.application.LetterBoxService;
import online.bottler.letter.application.LetterService;
import online.bottler.letter.application.ReplyLetterService;

public record LetterDeletionContext(
        LetterService letterService,
        ReplyLetterService replyLetterService,
        LetterBoxService letterBoxService,
        LetterKeywordService letterKeywordService
) {
}