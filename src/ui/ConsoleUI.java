package ui;

import game.RoundResult;
import models.Deck;
import models.Player;
import validation.PlayerCountValidator;
import validation.ShuffleCountValidator;
import validation.Validator;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Keeps all terminal input and output in one place.
 */
public class ConsoleUI {
    private final Scanner scanner;
    private final Validator<Integer> playerCountValidator;
    private final Validator<Integer> shuffleCountValidator;

    public ConsoleUI(Scanner scanner) {
        this(scanner, new PlayerCountValidator(), new ShuffleCountValidator());
    }

    public ConsoleUI(Scanner scanner, Validator<Integer> playerCountValidator,
                     Validator<Integer> shuffleCountValidator) {
        if (scanner == null || playerCountValidator == null || shuffleCountValidator == null) {
            throw new IllegalArgumentException("Scanner and validators must not be null.");
        }
        this.scanner = scanner;
        this.playerCountValidator = playerCountValidator;
        this.shuffleCountValidator = shuffleCountValidator;
    }

    public void showWelcome() {
        System.out.println("-----WELCOME TO WAR CARD GAME-----\n");
    }

    public String readInputFileName() {
        System.out.print("Enter input file name (e.g., input.txt): ");
        return scanner.nextLine().trim();
    }

    public int readPlayerCount() {
        return readInteger("Enter number of players (2-8): ", playerCountValidator);
    }

    public int readShuffleCount() {
        return readInteger("Enter number of shuffles (>= 1): ", shuffleCountValidator);
    }

    public void showOriginalDeck(Deck deck) {
        System.out.println("Original Deck: " + deck);
    }

    public void showShuffledDeck(Deck deck) {
        System.out.println("Shuffled Deck: " + deck);
    }

    public void showRoundStart(int roundNumber, List<Player> players) {
        System.out.println("\n--- Round " + roundNumber + " ---");
        for (Player player : players) {
            if (player.hasCards()) {
                System.out.println(player.getName() + " deck: " + player.getDeck());
            }
        }
    }

    public void showRoundResult(RoundResult result) {
        result.getPlayedCards().forEach((player, card) ->
                System.out.println(player.getName() + " played: " + card));
        System.out.println("Winner of Round " + result.getRoundNumber() + " is "
                + result.getWinner().getName());
        System.out.println(result.getWinner().getName() + " deck after Round "
                + result.getRoundNumber() + ": " + result.getWinner().getDeck());

        for (Player player : result.getEliminatedPlayers()) {
            System.out.println(">>> " + player.getName() + " has been ELIMINATED! <<<");
        }
    }

    /**
     * Lets the player control whether the next round should be displayed.
     * Pressing Enter advances one round; S removes the remaining pause prompts.
     */
    public RoundAction readRoundAction() {
        while (true) {
            System.out.print("Press Enter for the next round, or type S to skip to the end: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return RoundAction.CONTINUE;
            }
            if (input.equalsIgnoreCase("S")) {
                return RoundAction.SKIP_TO_END;
            }

            System.out.println("Invalid choice. Press Enter or type S.");
        }
    }

    public void showSkippingToEnd() {
        System.out.println("\nContinuing automatically while showing all round details...");
    }

    public void showLoadError(String fileName, Exception error) {
        System.out.println("Error: Could not read '" + fileName + "'. Please verify the filename and try again.");
        System.out.println(error.getMessage());
    }

    public void showWinner(Player winner, Path outputFile) {
        System.out.println("\n" + winner.getName() + " wins!");
        System.out.println("Winning deck successfully written to: " + outputFile.getFileName());
    }

    public boolean readPlayAgain() {
        while (true) {
            System.out.print("\nWould you like to play again? (Yes/No): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Yes")) {
                return true;
            }
            if (input.equalsIgnoreCase("No")) {
                return false;
            }

            System.out.println("Invalid choice. Please type Y or N.");
        }
    }

    public void showGoodbye() {
        System.out.println("Thanks for playing War!");
    }

    private int readInteger(String prompt, Validator<Integer> validator) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                validator.validate(value);
                return value;
            } catch (NumberFormatException error) {
                System.out.println("Invalid input. Please enter an integer.");
            } catch (IllegalArgumentException error) {
                System.out.println("Invalid input. " + error.getMessage());
            }
        }
    }
}
