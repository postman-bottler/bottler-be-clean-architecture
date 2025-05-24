package online.bottler.user.application.port.in;

import java.util.List;
import online.bottler.user.application.command.AuthEmailCommand;
import online.bottler.user.application.command.ChangePasswordCommand;
import online.bottler.user.application.command.CheckDuplicateNicknameCommand;
import online.bottler.user.application.command.CheckPasswordCommand;
import online.bottler.user.application.command.EmailCommand;
import online.bottler.user.application.command.NicknameCommand;
import online.bottler.user.application.command.ProfileImgCommand;
import online.bottler.user.application.command.SignInCommand;
import online.bottler.user.application.command.SignUpCommand;
import online.bottler.user.application.response.AccessTokenResponse;
import online.bottler.user.application.response.ExistingUserResponse;
import online.bottler.user.application.response.SignIn;
import online.bottler.user.application.response.UserResponse;
import online.bottler.user.domain.User;

public interface UserUseCase {
    void createUser(SignUpCommand signUpCommand);
    void createDeveloper(SignUpCommand signUpCommand);
    void checkEmail(EmailCommand emailCommand);
    void checkNickname(CheckDuplicateNicknameCommand checkDuplicateNicknameCommand);
    SignIn signin(SignInCommand signInCommand);
    AccessTokenResponse validateRefreshToken(String refreshToken);
    void deleteUser(CheckPasswordCommand checkPasswordCommand, String email);
    UserResponse findUser(String email);
    void updateNickname(NicknameCommand nicknameCommand, String email);
    void updatePassword(ChangePasswordCommand changePasswordCommand, String email);
    void updateProfileImage(ProfileImgCommand profileImgCommand, String email);
    void createProfileImg(ProfileImgCommand profileImgCommand);
    ExistingUserResponse findExistingUser(String nickname);
    void sendCodeToEmail(EmailCommand emailCommand);
    void verifyCode(AuthEmailCommand authEmailCommand);
    User findById(Long userId);
    SignIn kakaoSignin(String kakaoId, String nickname);
    String getProfileImageUrlById(Long userId);
    String getNicknameById(Long userId);
    void updateWarningCount(Long userId);
    List<Long> getAllUserIds();
    Long getUserIdByNickname(String nickname);
    void deleteEmailCode(AuthEmailCommand authEmailCommand);
}
