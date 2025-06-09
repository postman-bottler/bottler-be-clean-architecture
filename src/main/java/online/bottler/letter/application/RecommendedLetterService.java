package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.RecommendedLetterUseCase;
import online.bottler.letter.application.port.out.RecommendedLetterPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendedLetterService implements RecommendedLetterUseCase {

    private final RecommendedLetterPersistencePort recommendedLetterPersistencePort;

    @Transactional(readOnly = true)
    @Override
    public List<Long> findRecommendedLetterIdsByUserId(Long userId) {
        return recommendedLetterPersistencePort.findRecommendedLettersByUserId(userId);
    }
}
