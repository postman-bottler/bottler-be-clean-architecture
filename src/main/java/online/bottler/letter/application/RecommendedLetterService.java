package online.bottler.letter.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import online.bottler.letter.application.command.RecommendedLetterDTO;
import online.bottler.letter.application.port.out.RecommendedLetterRepository;

@Service
@RequiredArgsConstructor
public class RecommendedLetterService {

    private final RecommendedLetterRepository recommendedLetterRepository;

    public void saveRecommendedLetter(RecommendedLetterDTO recommendedLetterDTO) {
        recommendedLetterRepository.saveRecommendedLetter(recommendedLetterDTO.toDomain());
    }

    public List<Long> findRecommendedLetterIdsByUserId(Long userId) {
        return recommendedLetterRepository.findRecommendedLettersByUserId(userId);
    }
}
