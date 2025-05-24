package online.bottler.user.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import online.bottler.user.application.command.SignInCommand;

public record SignInRequest(
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$", message = "유효한 비밀번호 형식이 아닙니다.")
        String password
) {
        public SignInCommand toCommand() {
                return new SignInCommand(email, password);
        }
}
