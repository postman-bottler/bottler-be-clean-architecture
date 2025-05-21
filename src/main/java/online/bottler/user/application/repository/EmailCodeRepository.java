package online.bottler.user.application.repository;

import online.bottler.user.domain.EmailCode;

public interface EmailCodeRepository {
    void save(EmailCode emailCode);

    EmailCode findEmailCode(String email, String code);

    void deleteByEmail(String email);
}
