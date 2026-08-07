package validation;

/**
 * Validates the number of perfect shuffles to perform.
 */
public final class ShuffleCountValidator implements Validator<Integer> {
    public static final int MIN_SHUFFLES = 1;

    @Override
    public void validate(Integer shuffleCount) {
        if (shuffleCount == null || shuffleCount < MIN_SHUFFLES) {
            throw new IllegalArgumentException("Please enter an integer greater than or equal to "
                    + MIN_SHUFFLES + ".");
        }
    }
}
