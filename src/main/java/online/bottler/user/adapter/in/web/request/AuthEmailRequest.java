package online.bottler.user.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import online.bottler.user.application.command.AuthEmailCommand;

public record AuthEmailRequest(
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "인증코드는 필수 입력입니다.")
        String code
) {
        public AuthEmailCommand toCommand() {
                return new AuthEmailCommand(email, code);
        }
}
