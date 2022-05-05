package lazecoding.exception;

/**
 * 鉴权异常
 *
 * @author lazecoding
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String msg) {
        super(msg);
    }
}
