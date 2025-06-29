package online.bottler.letter.application.command;

import online.bottler.letter.domain.SortField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record CommonPageCommand(Integer page, Integer size, String sort) {

    public CommonPageCommand {
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 9 : size;
        sort = (sort == null || sort.isBlank()) ? "createdAt" : sort;

        SortField.validateSort(sort);
    }

    public Pageable toPageable() {
        return PageRequest.of(page - 1, size, Sort.by(sort).descending());
    }
}
