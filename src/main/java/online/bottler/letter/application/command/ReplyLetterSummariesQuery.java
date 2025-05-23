package online.bottler.letter.application.command;

import online.bottler.letter.adapter.in.web.request.PageRequest;

public record ReplyLetterSummariesQuery(Long letterId, PageRequest pageRequest, Long userId) {
    public static ReplyLetterSummariesQuery of(Long letterId, PageRequest pageRequest, Long userId) {
        return null;
    }
}
