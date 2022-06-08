package lazecoding.unique.exception;

/**
 * 受限操作异常
 *
 * @author lazecoding
 */
public class RestrictedOperationException extends RuntimeException {
    public RestrictedOperationException(String msg) {
        super(msg);
    }
}
