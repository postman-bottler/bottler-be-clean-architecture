package online.bottler.mapletter.application.port.in;

import java.util.List;
import online.bottler.mapletter.application.response.PaperResponse;

public interface PaperUseCase {
    List<PaperResponse> findPapers();
}
