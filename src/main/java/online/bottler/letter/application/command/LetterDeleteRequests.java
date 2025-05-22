package online.bottler.letter.application.command;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public class LetterDeleteRequests {

    private final List<LetterDeleteDTO> requests;

    public LetterDeleteRequests(List<LetterDeleteDTO> requests) {
        this.requests = List.copyOf(requests);
    }

    public Map<LetterType, Map<BoxType, List<Long>>> groupByTypeAndBox() {
        return requests.stream().collect(Collectors.groupingBy(LetterDeleteDTO::letterType,
                Collectors.groupingBy(LetterDeleteDTO::boxType,
                        Collectors.mapping(LetterDeleteDTO::letterId, Collectors.toList()))));
    }
}