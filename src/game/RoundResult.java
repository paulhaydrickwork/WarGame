package game;

import models.Card;
import models.Player;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * information about one completed round.
 */
public class RoundResult {
    private final int roundNumber;
    private final Map<Player, Card> playedCards;
    private final Player winner;
    private final List<Player> eliminatedPlayers;

    public RoundResult(int roundNumber, Map<Player, Card> playedCards, Player winner,
                       List<Player> eliminatedPlayers) {
        this.roundNumber = roundNumber;
        this.playedCards = Collections.unmodifiableMap(new LinkedHashMap<>(playedCards));
        this.winner = winner;
        this.eliminatedPlayers = List.copyOf(eliminatedPlayers);
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Map<Player, Card> getPlayedCards() {
        return playedCards;
    }

    public Player getWinner() {
        return winner;
    }

    public List<Player> getEliminatedPlayers() {
        return eliminatedPlayers;
    }
}
