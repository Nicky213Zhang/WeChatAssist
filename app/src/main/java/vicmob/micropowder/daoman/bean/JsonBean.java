package vicmob.micropowder.daoman.bean;

/**
 * Created by qq944 on 2017/9/29.
 */

public class JsonBean {
    private Boolean success;
    private Integer errorCode;
    private String msg;
    private Object body;
    public JsonBean() {
    }
    public JsonBean(Boolean success, Integer errorCode, String msg, Object body) {
        this.success = success;
        this.errorCode = errorCode;
        this.msg = msg;
        this.body = body;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
