package models;

public class Player {
    private final String name;
    private final Deck deck = new Deck();
    private boolean eliminated;

    public Player(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name must not be blank.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCardCount() {
        return deck.size();
    }

    public boolean hasCards() {
        return !deck.isEmpty();
    }

    public Card playTopCard() {
        return deck.drawTopCard();
    }

    public void receiveCard(Card card) {
        deck.addCard(card);
    }

    public void receiveCards(java.util.List<Card> cards) {
        deck.addCards(cards);
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void eliminate() {
        eliminated = true;
    }
}
