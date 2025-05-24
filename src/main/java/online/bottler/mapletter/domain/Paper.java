package online.bottler.mapletter.domain;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Paper {
    Long paperId;
    String paperUrl;
}
