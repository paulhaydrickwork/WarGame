package enums;

public enum Suit {
    C("C", "♣", 1), S("S", "♠", 2), H("H", "♥", 3), D("D", "♦", 4);

    private final String text;
    private final String symbol;
    private final int value;

    Suit(String text, String symbol, int value) {
        this.text = text;
        this.symbol = symbol;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String getSymbol() {
        return symbol;
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
