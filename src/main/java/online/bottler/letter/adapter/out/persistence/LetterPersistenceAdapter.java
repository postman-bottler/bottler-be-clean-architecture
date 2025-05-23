package online.bottler.letter.adapter.out.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.LetterEntity;
import online.bottler.letter.adapter.out.persistence.repository.LetterJpaRepository;
import online.bottler.letter.application.port.out.CreateLetterPort;
import online.bottler.letter.application.port.out.LoadLetterPort;
import online.bottler.letter.domain.Letter;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LetterPersistenceAdapter implements CreateLetterPort, LoadLetterPort {

    private final LetterJpaRepository letterJpaRepository;

    @Override
    public Letter create(Letter letter) {
        LetterEntity letterEntity = letterJpaRepository.save(LetterEntity.from(letter));
        return letterEntity.toDomain();
    }

    @Override
    public Optional<Letter> loadById(Long letterId) {
        return letterJpaRepository.findById(letterId).map(LetterEntity::toDomain);
    }
}
