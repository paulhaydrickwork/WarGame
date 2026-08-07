package validation;

/**
 * Validates the supported number of War-game players.
 */
public final class PlayerCountValidator implements Validator<Integer> {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 8;

    @Override
    public void validate(Integer playerCount) {
        if (playerCount == null || playerCount < MIN_PLAYERS || playerCount > MAX_PLAYERS) {
            throw new IllegalArgumentException("Please enter an integer between "
                    + MIN_PLAYERS + " and " + MAX_PLAYERS + ".");
        }
    }
}
