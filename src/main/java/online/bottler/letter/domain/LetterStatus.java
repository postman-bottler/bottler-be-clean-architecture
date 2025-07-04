package online.bottler.letter.domain;

import lombok.Getter;

@Getter
public enum LetterStatus {
    OPEN,
    BLOCKED,
    DELETED;

    public boolean isOpen() {
        return this == OPEN;
    }
}
