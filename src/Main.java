import java.nio.file.Path;
import java.util.Scanner;
import game.WarGame;
import models.Deck;
import models.Player;
import services.DeckFileService;
import services.GameSetupService;
import ui.ConsoleUI;
import ui.RoundAction;


public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleUI ui = new ConsoleUI(scanner);
            DeckFileService deckFileService = new DeckFileService(Path.of("resources"));
            GameSetupService gameSetup = new GameSetupService(ui, deckFileService);

            ui.showWelcome();
            boolean playAgain;
            do {
                Deck mainDeck = gameSetup.loadDeck();
                int playersCount = ui.readPlayerCount();
                int shuffles = ui.readShuffleCount();

                ui.showOriginalDeck(mainDeck);
                mainDeck.shuffle(shuffles);
                ui.showShuffledDeck(mainDeck);

                WarGame game = new WarGame(mainDeck, gameSetup.createPlayers(playersCount));
                boolean skipPauses = false;
                while (!game.isFinished()) {
                    ui.showRoundStart(game.getNextRoundNumber(), game.getPlayers());

                    var result = game.playRound();
                    ui.showRoundResult(result);

                    if (!skipPauses && !game.isFinished()
                            && ui.readRoundAction() == RoundAction.SKIP_TO_END) {
                        skipPauses = true;
                        ui.showSkippingToEnd();
                    }
                }

                Player winner = game.getWinner();
                Path outputFile = deckFileService.saveWinningDeck(winner.getDeck());
                ui.showWinner(winner, outputFile);
                playAgain = ui.readPlayAgain();
            } while (playAgain);

            ui.showGoodbye();
        } catch (Exception error) {
            System.err.println("Game ended because of an unexpected error: " + error.getMessage());
        }
    }
}
