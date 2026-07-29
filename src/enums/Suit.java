package enums;

public enum Suit {
    C("C", 1), S("S", 2), H("H", 3), D("D", 4);

    private final String text;
    private final int value;

    Suit(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Suit fromString(String text) {
        for (Suit s : Suit.values()) {
            if (s.text.equalsIgnoreCase(text)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown suit: " + text);
    }
}