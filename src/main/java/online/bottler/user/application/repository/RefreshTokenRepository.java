package online.bottler.user.application.repository;

import online.bottler.user.domain.RefreshToken;

public interface RefreshTokenRepository {
    void createRefreshToken(RefreshToken refreshToken);

    String findEmailByRefreshToken(String refreshToken);

    void deleteByEmail(String email);
}
