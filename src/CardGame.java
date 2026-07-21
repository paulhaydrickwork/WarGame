import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CardGame {
    public static void main(String[] args) {
        List<Card> mainDeck = new ArrayList<>();

        // 1. Read cards from input.txt using StringTokenizer
        try {
            String fileContent = Files.readString(Path.of("input.txt")).trim();
            StringTokenizer tokenizer = new StringTokenizer(fileContent, ",");

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                mainDeck.add(new Card(token.trim()));
            }
        } catch (IOException e) {
            System.err.println("Fatal Error: Could not read 'input.txt'. Make sure the file exists.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // 2. Input and validate playersCount (2 to 8)
        int playersCount = 0;
        while (playersCount < 2 || playersCount > 8) {
            System.out.print("Enter number of players (2-8): ");
            if (scanner.hasNextInt()) {
                playersCount = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        // 3. Input and validate shuffles (>= 1)
        int shuffles = 0;
        while (shuffles < 1) {
            System.out.print("Enter number of shuffles (>= 1): ");
            if (scanner.hasNextInt()) {
                shuffles = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        // Output initial deck
        System.out.println("Original Deck: " + mainDeck);

        // 4. Perfect riffle-shuffle simulation
        for (int i = 1; i <= shuffles; i++) {
            List<Card> half1 = new ArrayList<>();
            List<Card> half2 = new ArrayList<>();
            List<Card> newDeck = new ArrayList<>();

            int mid = mainDeck.size() / 2;
            for (int j = 0; j < mid; j++) {
                half1.add(mainDeck.get(j));
            }
            for (int j = mid; j < mainDeck.size(); j++) {
                half2.add(mainDeck.get(j));
            }

            for (int j = 0; j < mid; j++) {
                newDeck.add(half1.get(j));
                newDeck.add(half2.get(j));
            }
            mainDeck = newDeck;
        }

        // Output shuffled deck
        System.out.println("Shuffled Deck: " + mainDeck);

        // 5. Deal cards round-robin style
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playersCount; i++) {
            players.add(new Player());
        }

        int totalCardsCount = mainDeck.size();
        int dealIndex = 0;
        while (!mainDeck.isEmpty()) {
            players.get(dealIndex % playersCount).getDeck().add(mainDeck.remove(0));
            dealIndex++;
        }

        int round = 1;
        boolean hasAWinner = false;
        int winner = -1;

        // 6. Primary Game Loop
        while (!hasAWinner) {
            System.out.println("\n--- Round " + round + " ---");

            int roundWinner = -1;
            Card roundWinnerCard = null;

            // Find the first active player to anchor comparison
            for (int i = 0; i < playersCount; i++) {
                if (!players.get(i).getDeck().isEmpty()) {
                    roundWinner = i;
                    roundWinnerCard = players.get(i).getDeck().get(0);
                    break;
                }
            }

            if (roundWinner == -1) {
                break;
            }

            System.out.println("Player " + (roundWinner+1) + " played: " + roundWinnerCard);

            // Compare cards against all other active players
            for (int i = 0; i < playersCount; i++) {
                if (i == roundWinner || players.get(i).getDeck().isEmpty()) {
                    continue;
                }

                Card currentCard = players.get(i).getDeck().get(0);
                System.out.println("Player " + (i+1) + " played: " + currentCard);

                Rank currentRank = currentCard.getRank();
                Suit currentSuit = currentCard.getSuit();

                Rank winnerRank = roundWinnerCard.getRank();
                Suit winnerSuit = roundWinnerCard.getSuit();

                // Evaluate ranks using enum values
                if (currentRank.getValue() > winnerRank.getValue()) {
                    roundWinner = i;
                    roundWinnerCard = currentCard;
                }
                // Resolve ties using enum suit values
                else if (currentRank.getValue() == winnerRank.getValue()) {
                    if (currentSuit.getValue() > winnerSuit.getValue()) {
                        roundWinner = i;
                        roundWinnerCard = currentCard;
                    }
                }
            }

            System.out.println("Winner of Round " + round + " is Player " + (roundWinner+1));

            players.get(roundWinner).getDeck().add(players.get(roundWinner).getDeck().get(0));
            players.get(roundWinner).getDeck().remove(0);

            // 7. Collect cards from losers and append to winner's pile
            for (int i = 0; i < playersCount; i++) {
                if (i == roundWinner) {
                    continue;
                }
                if (!players.get(i).getDeck().isEmpty()) {
                    players.get(roundWinner).getDeck().add(players.get(i).getDeck().get(0));
                    players.get(i).getDeck().remove(0);
                }
            }

            System.out.println("Player " + (roundWinner+1) + " deck after Round " + round + ": " + players.get(roundWinner).getDeck());

            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted, shutting down gracefully.");
            }

            // 8. Check if a player holds the entire deck
            for (int i = 0; i < playersCount; i++) {
                if (players.get(i).getDeck().size() == totalCardsCount) {
                    hasAWinner = true;
                    winner = i;
                    break;
                }
            }

            round++;
        }

        if (winner != -1) {
            System.out.println("\nPlayer " + (winner + 1) + " wins!");
        }

        scanner.close();
    }
}