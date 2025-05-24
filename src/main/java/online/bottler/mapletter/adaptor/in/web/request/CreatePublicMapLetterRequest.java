package online.bottler.mapletter.adaptor.in.web.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import online.bottler.mapletter.application.command.CreatePublicMapLetterCommand;

public record CreatePublicMapLetterRequest(
        @NotBlank(message = "편지 제목은 생략이 불가능합니다.") String title,
        @NotBlank(message = "편지 내용은 생략이 불가능합니다.") String content,
        @NotBlank(message = "편지 위치 설명은 생략이 불가능합니다") String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String font,
        String paper,
        String label
) {
    public CreatePublicMapLetterCommand toCommand() {
        return new CreatePublicMapLetterCommand(title, content, description, latitude, longitude, font, paper, label);
    }
}
