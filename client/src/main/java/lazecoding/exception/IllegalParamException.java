package lazecoding.exception;

/**
 * 不合法参数
 *
 * @author lazecoding
 */
public class IllegalParamException extends RuntimeException {
    public IllegalParamException(String msg) {
        super(msg);
    }
}
