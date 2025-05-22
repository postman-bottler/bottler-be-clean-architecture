package online.bottler.mapletter.application.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record MapLetterPageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> MapLetterPageResponse<T> from(Page<T> page) {
        return new MapLetterPageResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
