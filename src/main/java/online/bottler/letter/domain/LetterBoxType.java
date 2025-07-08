package online.bottler.letter.domain;

import lombok.Getter;

@Getter
public class LetterBoxType {

    private final LetterType letterType;

    private final BoxType boxType;

    private LetterBoxType(LetterType letterType, BoxType boxType) {
        this.letterType = letterType;
        this.boxType = boxType;
    }

    public static LetterBoxType of(LetterType letterType, BoxType boxType) {
        return new LetterBoxType(letterType, boxType);
    }

    public static LetterBoxType from(String letterType, String boxType) {
        return LetterBoxType.of(LetterType.valueOf(letterType), BoxType.valueOf(boxType));
    }

    public boolean isValid() {
        return this.letterType != null && this.boxType != null;
    }
}
