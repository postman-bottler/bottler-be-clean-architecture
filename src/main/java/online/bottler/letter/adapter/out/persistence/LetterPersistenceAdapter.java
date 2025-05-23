package online.bottler.letter.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterJpaRepository;
import online.bottler.letter.application.port.out.CreateLetterPort;
import online.bottler.letter.application.port.out.DeleteLetterPort;
import online.bottler.letter.application.port.out.LoadLetterPort;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterPersistenceAdapter implements CreateLetterPort, LoadLetterPort, DeleteLetterPort {

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
