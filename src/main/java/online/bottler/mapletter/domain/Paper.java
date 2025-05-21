package online.bottler.mapletter.domain;

import lombok.*;
import online.bottler.mapletter.application.dto.PaperDTO;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Paper {
    Long paperId;
    String paperUrl;

    public static PaperDTO toPaperDTO(Paper paper) {
        return new PaperDTO(paper.paperId, paper.paperUrl);
    }
}
