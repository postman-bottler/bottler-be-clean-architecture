package online.bottler.mapletter.application.port.out;

import org.springframework.stereotype.Repository;
import online.bottler.mapletter.domain.Paper;
import java.util.List;

@Repository
public interface PaperPersistencePort {
    List<Paper> findAll();
}
