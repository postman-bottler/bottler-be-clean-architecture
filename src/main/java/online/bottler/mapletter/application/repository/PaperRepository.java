package online.bottler.mapletter.application.repository;

import org.springframework.stereotype.Repository;
import online.bottler.mapletter.domain.Paper;

import java.util.List;

@Repository
public interface PaperRepository {

    List<Paper> findAll();
}
