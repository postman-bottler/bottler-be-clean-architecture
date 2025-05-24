package online.bottler.letter.v2.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.infra.LetterJpaRepository;
import online.bottler.letter.infra.entity.LetterEntity;
import online.bottler.letter.v2.application.port.out.CreateLetterPersistencePort;
import online.bottler.letter.v2.application.port.out.DeleteLetterPersistencePort;
import online.bottler.letter.v2.application.port.out.LoadLetterPersistencePort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterPersistenceAdapter implements CreateLetterPersistencePort,
        LoadLetterPersistencePort,
        DeleteLetterPersistencePort {

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
    public void softDelete(Long letterId, Long userId, BoxType boxType) {
        letterJpaRepository.softDeleteById(letterId);
    }
}
