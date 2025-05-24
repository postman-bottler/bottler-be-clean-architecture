package online.bottler.user.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import online.bottler.user.application.command.EmailCommand;

public record EmailRequest(
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email
) {
        public EmailCommand toCommand() {
                return new EmailCommand(email);
        }
}
