package online.bottler.letter.domain;

import lombok.Getter;

@Getter
public enum LetterStatus {
    OPEN("OPEN"),
    BLOCKED("BLOCKED"),
    DELETED("DELETED");

    private final String status;

    LetterStatus(String status) {
        this.status = status;
    }

    public boolean isOpen() {
        return status.equals("OPEN");
    }
}
