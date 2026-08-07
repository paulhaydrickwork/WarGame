package models;

import enums.Rank;
import enums.Suit;

public class Card implements Comparable<Card> {
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

    public String toFileString() {
        return rawText;
    }

    @Override
    public int compareTo(Card other) {
        if (other == null) {
            throw new IllegalArgumentException("Card to compare must not be null.");
        }

        int rankComparison = Integer.compare(rank.getValue(), other.rank.getValue());
        return rankComparison != 0
                ? rankComparison
                : Integer.compare(suit.getValue(), other.suit.getValue());
    }

    @Override
    public String toString() {
        return rawText.substring(2) + suit.getSymbol();
    }
}
