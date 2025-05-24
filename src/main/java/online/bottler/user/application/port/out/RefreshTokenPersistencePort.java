package online.bottler.user.application.port.out;


import online.bottler.user.domain.RefreshToken;

public interface RefreshTokenPersistencePort {
    void createRefreshToken(RefreshToken refreshToken);

    String findEmailByRefreshToken(String refreshToken);

    void deleteByEmail(String email);
}
