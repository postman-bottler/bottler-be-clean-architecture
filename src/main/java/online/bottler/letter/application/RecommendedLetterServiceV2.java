package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.port.in.RecommendedLetterUseCase;
import online.bottler.letter.application.port.out.RecommendedLetterPersistencePort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendedLetterServiceV2 implements RecommendedLetterUseCase {

    private final RecommendedLetterPersistencePort recommendedLetterPersistencePort;

    @Override
    public List<Long> findRecommendedLetterIdsByUserId(Long userId) {
        return recommendedLetterPersistencePort.findRecommendedLettersByUserId(userId);
    }
}
