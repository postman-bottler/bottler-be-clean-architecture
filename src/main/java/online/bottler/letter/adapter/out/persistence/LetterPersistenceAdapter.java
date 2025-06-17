package online.bottler.letter.adapter.out.persistence;

import java.util.ArrayList;
import java.util.Collections;
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
    public Optional<Letter> loadById(Long id) {
        return letterJpaRepository.findById(id).map(LetterEntity::toDomain);
    }

    @Override
    public List<Letter> loadAllIncludingDeletedByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return LetterEntity.toDomainList(letterJpaRepository.findAllIncludingDeletedByIds(ids));
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
            result.addAll(letterJpaRepository.getRandomIds(count, randomId, excludedIds));
            tryCount++;
        }

        return result;
    }

    @Override
    public void softDelete(Long id) {
        letterJpaRepository.softDeleteById(id);
    }

    @Override
    public void softDeleteByIds(List<Long> ids) {
        letterJpaRepository.softDeleteByIds(ids);
    }

    @Override
    public void softBlock(Long id) {
        letterJpaRepository.softBlockById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return letterJpaRepository.existsById(id);
    }
}
