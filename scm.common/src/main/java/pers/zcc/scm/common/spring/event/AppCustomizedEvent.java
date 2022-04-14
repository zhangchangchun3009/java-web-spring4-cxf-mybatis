package pers.zcc.scm.common.spring.event;

/**
 * 应用自定义事件
 * @author zhangchangchun
 * @since 2022年4月11日
 * @param <T>
 */
public class AppCustomizedEvent<T> {
    private T message;

    protected boolean success;

    public AppCustomizedEvent(T what, boolean success) {
        this.message = what;
        this.success = success;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
