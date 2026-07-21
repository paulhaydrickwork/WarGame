public class Card {
    private final Rank rank;
    private final Suit suit;
    private final String rawText;

    public Card(String rawText) {
        this.rawText = rawText;
        this.suit = Suit.fromString(rawText.substring(0, 1));
        this.rank = Rank.fromString(rawText.substring(2));
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rawText;
    }
}