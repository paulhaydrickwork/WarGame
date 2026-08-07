package services;

import models.Card;
import models.Deck;
import validation.InputFileValidator;
import validation.Validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves decks in the comma-separated card-code file format.
 */
public class DeckFileService {
    private final Path inputDirectory;
    private final Validator<Path> inputFileValidator;

    public DeckFileService(Path inputDirectory) {
        this(inputDirectory, new InputFileValidator());
    }

    public DeckFileService(Path inputDirectory, Validator<Path> inputFileValidator) {
        if (inputDirectory == null || inputFileValidator == null) {
            throw new IllegalArgumentException("Input directory and validator must not be null.");
        }
        this.inputDirectory = inputDirectory;
        this.inputFileValidator = inputFileValidator;
    }

    public Deck load(String inputFileName) throws IOException {
        Path inputPath = inputDirectory.resolve(inputFileName).normalize();
        if (!inputPath.getParent().equals(inputDirectory.normalize())) {
            throw new IllegalArgumentException("Input file must be inside the resources directory.");
        }

        inputFileValidator.validate(inputPath);
        String content = Files.readString(inputPath, StandardCharsets.UTF_8);
        String[] cardCodes = content.split(",", -1);
        List<Card> cards = new ArrayList<>(cardCodes.length);

        for (String cardCode : cardCodes) {
            cards.add(new Card(cardCode.trim()));
        }
        return new Deck(cards);
    }

    public Path saveWinningDeck(Deck winningDeck) throws IOException {
        Files.createDirectories(inputDirectory);

        int fileIndex = 1;
        Path outputPath;
        do {
            outputPath = inputDirectory.resolve("input" + fileIndex + ".txt");
            fileIndex++;
        } while (Files.exists(outputPath));

        List<String> cardCodes = new ArrayList<>();
        for (Card card : winningDeck.getCards()) {
            cardCodes.add(card.toFileString());
        }

        Files.writeString(outputPath, String.join(",", cardCodes), StandardCharsets.UTF_8);
        return outputPath;
    }
}
