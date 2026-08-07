package validation;

import enums.Rank;
import enums.Suit;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates War-game deck files before they are converted to {@code Card} objects.
 * A valid file contains every card in a standard 52-card deck exactly once, separated
 * by commas.
 */
public final class InputFileValidator implements Validator<Path> {
    private static final int STANDARD_DECK_SIZE = Suit.values().length * Rank.values().length;

    /**
     * Reads and validates a file. The filename must follow the input*.txt convention.
     *
     * @throws IllegalArgumentException if the file name or content is invalid
     */
    @Override
    public void validate(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("Input file path must not be null.");
        }

        Path fileName = file.getFileName();
        if (fileName == null || !fileName.toString().matches("input.*\\.txt")) {
            throw new IllegalArgumentException("Filename must match input*.txt.");
        }
        if (!Files.isRegularFile(file) || !Files.isReadable(file)) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read.");
        }

        try {
            validateContent(Files.readString(file, StandardCharsets.UTF_8));
        } catch (java.io.IOException error) {
            throw new IllegalArgumentException("Input file could not be read.", error);
        }
    }

    /**
     * Validates deck content and reports every problem found in one exception.
     */
    public void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Input file is empty.");
        }

        String[] entries = content.split(",", -1); // Keep empty entries caused by leading/trailing/double commas.
        List<String> errors = new ArrayList<>();
        Set<String> cards = new HashSet<>();

        for (int index = 0; index < entries.length; index++) {
            int position = index + 1;
            String card = entries[index].trim();

            if (card.isEmpty()) {
                errors.add("Card " + position + " is empty (check for a missing card or extra comma).");
                continue;
            }
            if (!card.matches("[CDHS]-(?:[2-9]|10|[JQKA])")) {
                errors.add("Card " + position + " ('" + card
                        + "') must use SUIT-RANK format, e.g. H-A or S-10.");
                continue;
            }
            if (!cards.add(card)) {
                errors.add("Card " + position + " ('" + card + "') is duplicated.");
            }
        }

        if (entries.length != STANDARD_DECK_SIZE) {
            errors.add("A complete deck must contain " + STANDARD_DECK_SIZE + " cards; found " + entries.length + ".");
        }

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                String expectedCard = suit.name() + "-" + rankToText(rank);
                if (!cards.contains(expectedCard)) {
                    errors.add("Missing card: " + expectedCard + ".");
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid deck file:" + System.lineSeparator()
                    + String.join(System.lineSeparator(), errors));
        }
    }

    private static String rankToText(Rank rank) {
        return switch (rank) {
            case R_2 -> "2";
            case R_3 -> "3";
            case R_4 -> "4";
            case R_5 -> "5";
            case R_6 -> "6";
            case R_7 -> "7";
            case R_8 -> "8";
            case R_9 -> "9";
            case R_10 -> "10";
            case J -> "J";
            case Q -> "Q";
            case K -> "K";
            case A -> "A";
        };
    }
}
