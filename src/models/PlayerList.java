package models;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A list of players with operations that are specific to a War game.
 */
public class PlayerList extends ArrayList<Player> {

    public PlayerList() {
        super();
    }

    public PlayerList(Collection<? extends Player> players) {
        super(players);
    }

    public Player findWinner(int totalCards) {
        for (Player player : this) {
            if (player.getCardCount() == totalCards) {
                return player;
            }
        }
        return null;
    }
}
