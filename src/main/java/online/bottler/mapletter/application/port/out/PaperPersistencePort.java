package online.bottler.mapletter.application.port.out;

import online.bottler.mapletter.domain.Paper;
import java.util.List;

public interface PaperPersistencePort {
    List<Paper> findAll();
}
