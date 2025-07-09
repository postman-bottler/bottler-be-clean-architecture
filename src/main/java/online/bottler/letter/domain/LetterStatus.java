package online.bottler.letter.domain;

public enum LetterStatus {
    OPEN, BLOCKED, DELETED;

    public boolean isOpen() {
        return this == OPEN;
    }
}
