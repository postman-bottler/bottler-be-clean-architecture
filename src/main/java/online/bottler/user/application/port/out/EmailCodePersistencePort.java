package online.bottler.user.application.port.out;


import online.bottler.user.domain.EmailCode;

public interface EmailCodePersistencePort {
    void save(EmailCode emailCode);

    EmailCode findEmailCode(String email, String code);

    void deleteByEmail(String email);
}
