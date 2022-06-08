package lazecoding.unique.mvc;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * MVC ResultBean
 *
 * @author lazecoding
 */
public class ResultBean {

    /**
     * 附加信息
     */
    private String message = "";

    /**
     * 状态码
     */
    private String code = "200";

    /**
     * 是否成功
     */
    private boolean isSuccess = true;

    /**
     * 数据集合
     */
    private Map<String, Object> data = new HashMap(4);

    /**
     * 数据
     */
    private Object value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ResultBean addData(String key, Object value) {
        if (StringUtils.hasLength(key)) {
            this.data.put(key, value);
        }
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
