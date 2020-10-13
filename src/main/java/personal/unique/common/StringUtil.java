package personal.unique.common;

/**
 * @className: StringUtil
 * @package: personal.unique.common
 * @description:
 * @datetime: 2020/10/13   9:50
 * @author: lazecoding
 */
public class StringUtil {

    private StringUtil() {
    }

    public static String getString(Object str) {
        return str == null ? "" : str.toString();
    }

    public static boolean isEmpty(String str) {

        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
