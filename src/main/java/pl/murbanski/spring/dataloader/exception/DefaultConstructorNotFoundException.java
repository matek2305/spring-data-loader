package pl.murbanski.spring.dataloader.exception;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public class DefaultConstructorNotFoundException extends RuntimeException {

    public DefaultConstructorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
