package online.bottler.user.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import online.bottler.user.application.command.ProfileImgCommand;

public record ProfileImgRequest(
        @NotBlank(message = "이미지 URL은 필수 입력입니다.")
        String imageUrl
) {
        public ProfileImgCommand toCommand() {
                return new ProfileImgCommand(imageUrl);
        }
}
