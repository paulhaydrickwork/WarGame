import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import enums.*;
import models.*;


public class CardGame {
    public static void main(String[] args) {
        System.out.println("WELCOME TO WAR CARD GAME");
        Scanner scanner = new Scanner(System.in);

        List<Card> mainDeck = loadDeck(scanner);
        int playersCount = readPlayersCount(scanner);
        int shuffles = readShuffleCount(scanner);

        System.out.println("Original Deck: " + mainDeck);
        mainDeck = shuffle(mainDeck, shuffles);
        System.out.println("Shuffled Deck: " + mainDeck);

        List<Player> players = createPlayers(playersCount);
        int totalCardsCount = mainDeck.size();
        dealCards(mainDeck, players);

        int winner = playGame(players, totalCardsCount);
        if (winner != -1) {
            System.out.println("\nPlayer " + (winner + 1) + " wins!");
            writeWinningDeck(players.get(winner).getDeck());
        }

        scanner.close();
    }

    private static List<Card> loadDeck(Scanner scanner) {
        List<Card> mainDeck = new ArrayList<>();
        boolean fileLoaded = false;

        while (!fileLoaded) {
            System.out.print("Enter input file name (e.g., input.txt): ");
            String inputFileName = scanner.nextLine().trim();

            try {
                String fileContent = Files.readString(Path.of("inputs", inputFileName)).trim();
                StringTokenizer tokenizer = new StringTokenizer(fileContent, ",");

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    mainDeck.add(new Card(token.trim()));
                }

                fileLoaded = true;
            } catch (IOException e) {
                System.out.println("Error: Could not read '" + inputFileName + "'. Please verify the filename and try again.\n");
            }
        }

        return mainDeck;
    }

    private static int readPlayersCount(Scanner scanner) {
        int playersCount = 0;
        while (playersCount < 2 || playersCount > 8) {
            System.out.print("Enter number of players (2-8): ");
            if (scanner.hasNextInt()) {
                playersCount = scanner.nextInt();
            } else {
                scanner.next();
            }

            if (playersCount < 2 || playersCount > 8) {
                System.out.println("Invalid input. Please enter a number between 2 and 8.");
            }
        }

        return playersCount;
    }

    private static int readShuffleCount(Scanner scanner) {
        int shuffles = 0;
        while (shuffles < 1) {
            System.out.print("Enter number of shuffles (>= 1): ");

            if (scanner.hasNextInt()) {
                shuffles = scanner.nextInt();
            } else {
                scanner.next();
            }

            if (shuffles < 1) {
                System.out.println("Invalid input. Please enter a number greater than or equal to 1.");
            }
        }

        return shuffles;
    }

    private static List<Card> shuffle(List<Card> mainDeck, int shuffles) {
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

        return mainDeck;
    }

    private static List<Player> createPlayers(int playersCount) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playersCount; i++) {
            players.add(new Player());
        }

        return players;
    }

    private static void dealCards(List<Card> mainDeck, List<Player> players) {
        int dealIndex = 0;
        while (!mainDeck.isEmpty()) {
            players.get(dealIndex % players.size()).getDeck().add(mainDeck.remove(0));
            dealIndex++;
        }
    }

    private static int playGame(List<Player> players, int totalCardsCount) {
        int playersCount = players.size();
        boolean[] eliminated = new boolean[playersCount];

        int round = 1;
        boolean hasAWinner = false;
        int winner = -1;

        while (!hasAWinner) {
            System.out.println("\n--- Round " + round + " ---");

            for (int i = 0; i < playersCount; i++) {
                if (!players.get(i).getDeck().isEmpty()) {
                    System.out.println("Player " + (i + 1) + " deck: " + players.get(i).getDeck());
                }
            }

            int roundWinner = -1;
            Card roundWinnerCard = null;

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

            System.out.println("Player " + (roundWinner + 1) + " played: " + roundWinnerCard);

            for (int i = 0; i < playersCount; i++) {
                if (i == roundWinner || players.get(i).getDeck().isEmpty()) {
                    continue;
                }

                Card currentCard = players.get(i).getDeck().get(0);
                System.out.println("Player " + (i + 1) + " played: " + currentCard);

                Rank currentRank = currentCard.getRank();
                Suit currentSuit = currentCard.getSuit();

                Rank winnerRank = roundWinnerCard.getRank();
                Suit winnerSuit = roundWinnerCard.getSuit();

                if (currentRank.getValue() > winnerRank.getValue()) {
                    roundWinner = i;
                    roundWinnerCard = currentCard;
                }
                else if (currentRank.getValue() == winnerRank.getValue()) {
                    if (currentSuit.getValue() > winnerSuit.getValue()) {
                        roundWinner = i;
                        roundWinnerCard = currentCard;
                    }
                }
            }

            System.out.println("Winner of Round " + round + " is Player " + (roundWinner + 1));

            players.get(roundWinner).getDeck().add(players.get(roundWinner).getDeck().get(0));
            players.get(roundWinner).getDeck().remove(0);

            for (int i = 0; i < playersCount; i++) {
                if (i == roundWinner) {
                    continue;
                }
                if (!players.get(i).getDeck().isEmpty()) {
                    players.get(roundWinner).getDeck().add(players.get(i).getDeck().get(0));
                    players.get(i).getDeck().remove(0);
                }
            }

            System.out.println("Player " + (roundWinner + 1) + " deck after Round " + round + ": " + players.get(roundWinner).getDeck());

            for (int i = 0; i < playersCount; i++) {
                if (!eliminated[i] && players.get(i).getDeck().isEmpty()) {
                    eliminated[i] = true;
                    System.out.println(">>> Player " + (i + 1) + " has been ELIMINATED! <<<");
                }
            }

            for (int i = 0; i < playersCount; i++) {
                if (players.get(i).getDeck().size() == totalCardsCount) {
                    hasAWinner = true;
                    winner = i;
                    break;
                }
            }

            round++;
        }

        return winner;
    }

    private static void writeWinningDeck(List<Card> winningDeck) {
        int fileIndex = 1;
        File outputFile;
        do {
            outputFile = new File("inputs", "input" + fileIndex + ".txt");
            fileIndex++;
        } while (outputFile.exists());

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            for (int i = 0; i < winningDeck.size(); i++) {
                writer.print(winningDeck.get(i));
                if (i < winningDeck.size() - 1) {
                    writer.print(", ");
                }
            }
            System.out.println("Winning deck successfully written to: " + outputFile.getName());
        } catch (IOException e) {
            System.err.println("Error writing winning deck to output file: " + e.getMessage());
        }
    }
}
