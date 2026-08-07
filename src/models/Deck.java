package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A mutable, ordered collection of playing cards.
 */
public class Deck {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public Card drawTopCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw a card from an empty deck.");
        }
        return cards.remove(0);
    }

    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card must not be null.");
        }
        cards.add(card);
    }

    public void addCards(List<Card> cardsToAdd) {
        if (cardsToAdd == null || cardsToAdd.contains(null)) {
            throw new IllegalArgumentException("Cards must not be null.");
        }
        cards.addAll(cardsToAdd);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }


    public void shuffle(int times) {
        if (times < 0) {
            throw new IllegalArgumentException("Shuffle count must be at least 0.");
        }
        if (cards.size() % 2 != 0) {
            throw new IllegalStateException("A perfect shuffle requires an even number of cards.");
        }

        for (int shuffle = 0; shuffle < times; shuffle++) {
            int middle = cards.size() / 2;
            List<Card> shuffled = new ArrayList<>(cards.size());

            for (int index = 0; index < middle; index++) {
                shuffled.add(cards.get(index));
                shuffled.add(cards.get(middle + index));
            }

            cards.clear();
            cards.addAll(shuffled);
        }
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
