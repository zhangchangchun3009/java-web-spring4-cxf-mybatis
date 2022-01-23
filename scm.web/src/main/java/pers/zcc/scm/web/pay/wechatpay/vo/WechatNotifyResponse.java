package pers.zcc.scm.web.pay.wechatpay.vo;

public class WechatNotifyResponse {
    // 错误码，SUCCESS为清算机构接收成功，其他错误码为失败
    private String code;

    // 返回信息，如非空，为错误原因
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
