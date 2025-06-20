package online.bottler.letter.adapter.out.cache;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bottler.letter.adapter.out.cache.repository.LetterRedisRepository;
import online.bottler.letter.application.port.out.LetterCachePort;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LetterCacheAdapter implements LetterCachePort {

    private final LetterRedisRepository letterRedisRepository;

    @Override
    public List<Long> fetchActiveByUserId(Long userId) {
        return letterRedisRepository.fetchActiveByUserId(userId);
    }
}
