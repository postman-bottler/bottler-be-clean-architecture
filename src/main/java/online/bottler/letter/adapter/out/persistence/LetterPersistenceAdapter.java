package online.bottler.letter.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterJpaRepository;
import online.bottler.letter.application.port.out.LetterPersistencePort;
import online.bottler.letter.domain.Letter;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterPersistenceAdapter implements LetterPersistencePort {

    private final LetterJpaRepository letterJpaRepository;

    @Override
    public Letter create(Letter letter) {
        return letterJpaRepository.save(LetterEntity.from(letter)).toDomain();
    }

    @Override
    public Optional<Letter> loadById(Long letterId) {
        return letterJpaRepository.findById(letterId).map(LetterEntity::toDomain);
    }

    @Override
    public List<Letter> loadAllByIds(List<Long> letterIds) {
        return LetterEntity.toDomainList(letterJpaRepository.findAllByIds(letterIds));
    }

    @Override
    public List<Letter> loadAllByUserId(Long userId) {
        return LetterEntity.toDomainList(letterJpaRepository.findAllByUserId(userId));
    }

    @Override
    public List<Long> loadIdsByUserId(Long userId) {
        return letterJpaRepository.findIdsByUserId(userId);
    }

    @Override
    public List<Long> fetchRandomLetterIdsExcluding(int count, List<Long> excludedIds) {
        Long maxId = letterJpaRepository.findMaxId();

        if (maxId == null || maxId == 0) {
            return new ArrayList<>();
        }

        List<Long> result = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int tryCount = 0;

        while (result.size() < count && tryCount < 5) {
            long randomId = random.nextLong(1L, maxId + 1);

            List<Long> partial = letterJpaRepository.getRandomIds(count, randomId, excludedIds);

            result.addAll(partial);
            tryCount++;
        }

        return result;
    }

    @Override
    public void softDelete(Long letterId) {
        letterJpaRepository.softDeleteById(letterId);
    }

    @Override
    public void softDeleteByIds(List<Long> letterIds) {
        letterJpaRepository.softDeleteByIds(letterIds);
    }

    @Override
    public void softBlock(Long id) {
        letterJpaRepository.softBlockById(id);
    }

    @Override
    public boolean existsById(Long letterId) {
        return letterJpaRepository.existsById(letterId);
    }
}
