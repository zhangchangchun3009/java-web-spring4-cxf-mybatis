package pers.zcc.scm.web.pay.wechatpay.vo;

public class PaymentVO {
    /**
     * 商户订单号，商户网站订单系统中唯一订单号，必填
     */
    private String outTradeNo;

    /**
     * 付款金额，分。 必填
     */
    private String total;
    /**
     * 商品描述，必填
     */
    private String description;

    private String payerClientIp;

    private String code;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the payerClientIp
     */
    public String getPayerClientIp() {
        return payerClientIp;
    }

    /**
     * @param payerClientIp the payerClientIp to set
     */
    public void setPayerClientIp(String payerClientIp) {
        this.payerClientIp = payerClientIp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
