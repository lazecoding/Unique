package lazecoding.unique.config;

/**
 *
 * 服务端常量
 *
 * @author lazecoding
 */
public class ServerConstant {


    /**
     * 最大申请步长
     */
    public static final int MAX_STEP = 1000000;

    /**
     * 最小申请步长
     */
    public static final int MIN_STEP = 20000;


    /**
     * 最大初始化步长(应该 <= 最大申请步长)
     */
    public static final int MAX_INIT_STEP = 1000000;

    /**
     * 最小初始化步长（应该 = 最小申请步长）
     */
    public static final int MIN_INIT_STEP = 20000;

}
