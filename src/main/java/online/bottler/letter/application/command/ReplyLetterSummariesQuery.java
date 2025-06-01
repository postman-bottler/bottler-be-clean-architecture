package online.bottler.letter.application.command;

import online.bottler.letter.adapter.in.web.request.CommonPageRequest;

public record ReplyLetterSummariesQuery(Long letterId, CommonPageRequest commonPageRequest, Long userId) {
    public static ReplyLetterSummariesQuery of(Long letterId, CommonPageRequest commonPageRequest, Long userId) {
        return new ReplyLetterSummariesQuery(letterId, commonPageRequest, userId);
    }
}
