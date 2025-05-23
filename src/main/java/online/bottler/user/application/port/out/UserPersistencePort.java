package online.bottler.user.application.port.out;

import java.util.List;
import online.bottler.user.domain.Ban;
import online.bottler.user.domain.User;

public interface UserPersistencePort {
    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    User findByEmail(String email);

    void softDeleteUser(Long userId);

    void updateNickname(Long userId, String nickname);

    void updatePassword(Long userId, String password);

    void updateProfileImageUrl(Long userId, String imageUrl);

    User findById(Long userId);

    void updateUsers(List<User> users);

    List<User> findWillBeUnbannedUsers(List<Ban> bans);

    boolean existsByEmailAndProvider(String kakaoId);

    List<User> findAllUserId();

    void updateWarningCount(User user);

    User findByNickname(String nickname);
}
