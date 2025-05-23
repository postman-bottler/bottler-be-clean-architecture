package online.bottler.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class User {
    private static final int MAX_WARNING_COUNT = 3;

    private Long userId;
    private final String email;
    private String password;
    private String nickname;
    private String imageUrl;
    private Role role;
    private Provider provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private int warningCount;

    private User(Long userId, String email, String password, String nickname, String imageUrl, Role role,
                 Provider provider, LocalDateTime createdAt,
                 LocalDateTime updatedAt, boolean isDeleted, int warningCount) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
        this.provider = provider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.warningCount = warningCount;
    }

    private User(String email, String password, String nickname, String imageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = Role.USER;
        this.provider = Provider.LOCAL;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.warningCount = 0;
    }

    private User(String email, String nickname, String imageUrl, String password, Provider provider) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = Role.USER;
        this.provider = provider;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.warningCount = 0;
    }

    private User(String email, String password, String nickname, String imageUrl, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
        this.provider = Provider.LOCAL;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.warningCount = 0;
    }

    public static User createUser(Long userId, String email, String password, String nickname, String imageUrl,
                                  Role role, Provider provider, LocalDateTime createdAt, LocalDateTime updatedAt,
                                  boolean isDeleted, int warningCount) {
        return new User(userId, email, password, nickname, imageUrl, role, provider, createdAt, updatedAt, isDeleted,
                warningCount);
    }

    public static User createUser(String email, String password, String nickname, String imageUrl) { //로컬 회원가입
        return new User(email, password, nickname, imageUrl);
    }

    public static User createKakaoUser(String email, String nickname, String imageUrl, String password) { //카카오 로그인
        return new User(email, nickname, imageUrl, password, Provider.KAKAO);
    }

    public static User createDeveloper(String email, String password, String nickname, String imageUrl) { //개발자 계정
        return new User(email, password, nickname, imageUrl, Role.DEVELOPER);
    }

    public void updateWarningCount() {
        this.warningCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean checkBan() {
        if (this.warningCount >= MAX_WARNING_COUNT) {
            this.warningCount = 0;
            this.updatedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean isBanned() {
        return this.role == Role.BAN_USER;
    }

    public void unban() {
        this.role = Role.USER;
    }

    public void banned() {
        this.role = Role.BAN_USER;
    }
}
