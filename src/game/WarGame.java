package game;

import models.Card;
import models.Deck;
import models.Player;
import models.PlayerList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the rules and state of a game of War.
 */
public class WarGame {
    private final PlayerList players;
    private final int totalCards;
    private int nextRoundNumber = 1;

    public WarGame(Deck deck, PlayerList players) {
        if (deck == null || players == null || players.size() < 2) {
            throw new IllegalArgumentException("A game requires a deck and at least two players.");
        }

        this.players = new PlayerList(players);
        this.totalCards = deck.size();
        deal(deck);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getNextRoundNumber() {
        return nextRoundNumber;
    }

    public boolean isFinished() {
        return getWinner() != null;
    }

    public Player getWinner() {
        return players.findWinner(totalCards);
    }

    public RoundResult playRound() {
        if (isFinished()) {
            throw new IllegalStateException("The game has already finished.");
        }

        Map<Player, Card> playedCards = new LinkedHashMap<>();
        Player roundWinner = null;
        Card winningCard = null;

        for (Player player : players) {
            if (!player.hasCards()) {
                continue;
            }

            Card playedCard = player.playTopCard();
            playedCards.put(player, playedCard);

            if (winningCard == null || playedCard.compareTo(winningCard) > 0) {
                roundWinner = player;
                winningCard = playedCard;
            }
        }

        if (roundWinner == null) {
            throw new IllegalStateException("No player can play a card.");
        }

        List<Card> wonCards = new ArrayList<>();
        wonCards.add(playedCards.get(roundWinner));
        for (Map.Entry<Player, Card> play : playedCards.entrySet()) {
            if (play.getKey() != roundWinner) {
                wonCards.add(play.getValue());
            }
        }
        roundWinner.receiveCards(wonCards);

        List<Player> eliminatedPlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.hasCards() && !player.isEliminated()) {
                player.eliminate();
                eliminatedPlayers.add(player);
            }
        }

        RoundResult result = new RoundResult(nextRoundNumber, playedCards, roundWinner, eliminatedPlayers);
        nextRoundNumber++;
        return result;
    }

    private void deal(Deck deck) {
        int playerIndex = 0;
        while (!deck.isEmpty()) {
            players.get(playerIndex % players.size()).receiveCard(deck.drawTopCard());
            playerIndex++;
        }
    }
}
