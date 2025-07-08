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
import online.bottler.letter.domain.LetterStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterPersistenceAdapter implements LetterPersistencePort {

    private final LetterJpaRepository letterJpaRepository;

    @Override
    public Letter save(Letter letter) {
        return letterJpaRepository.save(LetterEntity.from(letter)).toDomain();
    }

    @Override
    public void saveAll(List<Letter> letters) {
        letterJpaRepository.saveAll(LetterEntity.fromList(letters));
    }

    @Override
    public Optional<Letter> loadByIdAndStatus(Long id, LetterStatus status) {
        return letterJpaRepository.findByIdAndStatus(id, status).map(LetterEntity::toDomain);
    }

    @Override
    public List<Letter> loadAllByIdIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return LetterEntity.toDomainList(letterJpaRepository.findAllByIdIn(ids));
    }

    @Override
    public List<Letter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus status) {
        return LetterEntity.toDomainList(letterJpaRepository.findAllByIdInAndStatus(ids, status));
    }

    @Override
    public List<Long> loadIdsByUserIdAndStatus(Long userId, LetterStatus status) {
        return letterJpaRepository.findIdsByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Long> loadRandomLetterIdsByIdNotInAndStatus(List<Long> excludedIds, LetterStatus status,  int count) {
        Optional<Long> maxId = letterJpaRepository.findMaxIdByStatus(status);

        if (maxId.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> result = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int tryCount = 0;

        while (result.size() < count && tryCount < 5) {
            long randomId = random.nextLong(1L, maxId.get() + 1);
            result.addAll(letterJpaRepository.findIdsByIdNotInAndStatus(count, randomId, excludedIds, status));
            tryCount++;
        }

        return result;
    }

    @Override
    public boolean existsByIdAndStatus(Long id, LetterStatus status) {
        return letterJpaRepository.existsByIdAndStatus(id, status);
    }
}
