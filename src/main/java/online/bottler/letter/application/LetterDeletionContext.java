package online.bottler.letter.application;

public record LetterDeletionContext(
        LetterService letterService,
        ReplyLetterService replyLetterService,
        LetterBoxService letterBoxService,
        LetterKeywordService letterKeywordService
) {
}