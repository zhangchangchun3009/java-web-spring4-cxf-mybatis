package pers.zcc.scm.web.pay.wechatpay.vo;

public class Amount {
    // 订单总金额，单位为分。
    private Integer total;
    // 用户支付金额，单位为分
    private Integer payer_total;
    // CNY：人民币，境内商户号仅支持人民币
    private String currency;
    // 用户支付币种
    private String payer_currency;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPayer_total() {
        return payer_total;
    }

    public void setPayer_total(Integer payer_total) {
        this.payer_total = payer_total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayer_currency() {
        return payer_currency;
    }

    public void setPayer_currency(String payer_currency) {
        this.payer_currency = payer_currency;
    }

}
