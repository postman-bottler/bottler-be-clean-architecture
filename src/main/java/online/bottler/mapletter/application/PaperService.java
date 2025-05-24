package online.bottler.mapletter.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.mapletter.application.port.in.PaperUseCase;
import online.bottler.mapletter.application.port.out.PaperPersistencePort;
import online.bottler.mapletter.application.response.PaperResponse;
import online.bottler.mapletter.domain.Paper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperService implements PaperUseCase {
    private final PaperPersistencePort paperPersistencePort;

    @Transactional(readOnly = true)
    public List<PaperResponse> findPapers() {
        List<Paper> papers = paperPersistencePort.findAll();
        return papers.stream()
                .map(PaperResponse::from)
                .toList();
    }
}
