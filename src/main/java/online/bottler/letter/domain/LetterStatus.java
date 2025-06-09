package online.bottler.letter.domain;

import lombok.Getter;

@Getter
public class LetterStatus {
    private final boolean isDeleted;
    private final boolean isBlocked;

    public LetterStatus(boolean isDeleted, boolean isBlocked) {
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
    }

    public static LetterStatus create(boolean isDeleted, boolean isBlocked) {
        return new LetterStatus(isDeleted, isBlocked);
    }
}
