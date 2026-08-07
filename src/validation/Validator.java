package validation;

/**
 * Validates a value and reports why it is invalid.
 *
 * @param <T> the type of value to validate
 */
public interface Validator<T> {
    /**
     * @throws IllegalArgumentException when {@code value} is invalid
     */
    void validate(T value);
}
