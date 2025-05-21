package online.bottler.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import online.bottler.user.domain.EmailCode;
import online.bottler.user.exception.EmailCodeException;
import online.bottler.user.infra.entity.EmailCodeEntity;
import online.bottler.user.application.repository.EmailCodeRepository;

@Repository
@RequiredArgsConstructor
public class EmailCodeRepositoryImpl implements EmailCodeRepository {
    private final EmailCodeJpaRepository emailCodeJpaRepository;

    @Override
    public void save(EmailCode emailCode) {
        EmailCodeEntity emailCodeEntity = emailCodeJpaRepository.findByEmail(emailCode.getEmail());

        if (emailCodeEntity != null) {
            emailCodeEntity.changeCode(emailCode.getCode());
        } else {
            emailCodeJpaRepository.save(EmailCodeEntity.from(emailCode));
        }
    }

    @Override
    public EmailCode findEmailCode(String email, String code) {
        EmailCodeEntity emailCodeEntity = emailCodeJpaRepository.findByEmailAndCode(email, code)
                .orElseThrow(() -> new EmailCodeException("유효하지 않은 인증코드입니다."));
        return EmailCodeEntity.toEmailCode(emailCodeEntity);
    }

    @Override
    public void deleteByEmail(String email) {
        emailCodeJpaRepository.deleteByEmail(email);
    }
}
