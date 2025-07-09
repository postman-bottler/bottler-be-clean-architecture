package online.bottler.letter.domain;

public record LetterBoxType(LetterType letterType, BoxType boxType) {

    public static LetterBoxType of(LetterType letterType, BoxType boxType) {
        return new LetterBoxType(letterType, boxType);
    }

    public static LetterBoxType from(String letterType, String boxType) {
        return LetterBoxType.of(LetterType.valueOf(letterType), BoxType.valueOf(boxType));
    }

    public boolean isValid() {
        return letterType != null && boxType != null;
    }
}
