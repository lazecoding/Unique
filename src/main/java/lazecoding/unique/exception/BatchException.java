package lazecoding.unique.exception;

/**
 * 批量操作异常
 *
 * @author liux
 */
public class BatchException extends RuntimeException {
    public BatchException(String msg) {
        super(msg);
    }
}
