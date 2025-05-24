package online.bottler.mapletter.application.response;

import online.bottler.mapletter.domain.Paper;

public record PaperResponse(
        Long paperId,
        String paperUrl
) {
    public static PaperResponse from(Paper paper) {
        return new PaperResponse(paper.getPaperId(), paper.getPaperUrl());
    }
}
