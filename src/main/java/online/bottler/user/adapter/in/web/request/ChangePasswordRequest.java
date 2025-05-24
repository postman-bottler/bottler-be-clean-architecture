package online.bottler.user.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import online.bottler.user.application.command.ChangePasswordCommand;

public record ChangePasswordRequest(
        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$", message = "유효한 비밀번호 형식이 아닙니다.")
        String existingPassword,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$", message = "유효한 비밀번호 형식이 아닙니다.")
        String newPassword
) {
        public ChangePasswordCommand toCommand() {
                return new ChangePasswordCommand(existingPassword, newPassword);
        }
}
