package models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> deck = new ArrayList<>();

    public List<Card> getDeck() {
        return deck;
    }
}