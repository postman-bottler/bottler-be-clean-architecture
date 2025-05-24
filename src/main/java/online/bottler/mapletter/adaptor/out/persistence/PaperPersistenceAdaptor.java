package online.bottler.mapletter.adaptor.out.persistence;

import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.adaptor.out.persistence.repository.PaperJpaRepository;
import org.springframework.stereotype.Repository;
import online.bottler.mapletter.domain.Paper;
import online.bottler.mapletter.adaptor.out.persistence.entity.PaperEntity;
import online.bottler.mapletter.application.port.out.PaperPersistencePort;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaperPersistenceAdaptor implements PaperPersistencePort {
    private final PaperJpaRepository paperJpaRepository;

    @Override
    public List<Paper> findAll() {
        List<PaperEntity> findAllPapers = paperJpaRepository.findAll();

        return findAllPapers.stream()
                .map(PaperEntity::toDomain)
                .toList();
    }
}
