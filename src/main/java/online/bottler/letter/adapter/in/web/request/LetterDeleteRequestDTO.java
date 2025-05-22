package online.bottler.letter.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;
import online.bottler.letter.domain.BoxType;

public record LetterDeleteRequestDTO(@NotNull(message = "Letter ID는 필수입니다.") Long letterId,
                                     @NotNull(message = "Box Type은 필수입니다.") BoxType boxType) {
}
