package services;

import models.Deck;
import models.Player;
import models.PlayerList;
import ui.ConsoleUI;


/**
 * Coordinates game setup that requires both console input and deck loading.
 */
public class GameSetupService {
    private final ConsoleUI ui;
    private final DeckFileService deckFileService;

    public GameSetupService(ConsoleUI ui, DeckFileService deckFileService) {
        this.ui = ui;
        this.deckFileService = deckFileService;
    }

    public Deck loadDeck() {
        while (true) {
            String inputFileName = ui.readInputFileName();
            try {
                return deckFileService.load(inputFileName);
            } catch (Exception error) {
                ui.showLoadError(inputFileName, error);
            }
        }
    }

    public PlayerList createPlayers(int playerCount) {
        PlayerList players = new PlayerList();
        for (int index = 0; index < playerCount; index++) {
            players.add(new Player("Player " + (index + 1)));
        }
        return players;
    }
}
