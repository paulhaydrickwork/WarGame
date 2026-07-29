package enums;

public enum Rank {
    R_2("2", 1), R_3("3", 2), R_4("4", 3), R_5("5", 4),
    R_6("6", 5), R_7("7", 6), R_8("8", 7), R_9("9", 8),
    R_10("10", 9), J("J", 10), Q("Q", 11), K("K", 12), A("A", 13);

    private final String text;
    private final int value;

    Rank(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Rank fromString(String text) {
        for (Rank r : Rank.values()) {
            if (r.text.equalsIgnoreCase(text)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown rank: " + text);
    }
}