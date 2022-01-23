package pers.zcc.scm.web.pay.alipay.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlipayNotifyMessage {
    // 通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss。示例值：2015-14-27 15:45:58
    private String notify_time;

    // 通知的类型。示例值：trade_status_sync
    private String notify_type;

    // 通知校验 ID。示例值：ac05099524730693a8b330c5ecf72da9786
    private String notify_id;

    // 支付宝分配给开发者的应用 ID。示例值：2014072300007148
    private String app_id;

    private String charset;

    private String version;

    // 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2。示例值：RSA2
    private String sign_type;

    // 签名。详见下文 异步返回结果的验签。示例值：601510b7970e52cc63db0f44997cf70e
    private String sign;

    // 支付宝交易凭证号。 示例值：2013112011001004330000121536
    private String trade_no;

    // 原支付请求的商户订单号。示例值：6823789339978248
    private String out_trade_no;

    // 商户业务 ID，主要是退款通知中返回退款申请的流水号。示例值：HZRF001
    private String out_biz_no;

    // 买家支付宝账号对应的支付宝唯一用户号。以 2088 开头的纯 16 位数字。示例值：2088102122524333
    private String buyer_id;

    // 买家支付宝账号。示例值：159﹡﹡﹡﹡﹡﹡20
    private String buyer_logon_id;

    // 卖家支付宝用户号。示例值：2088101106499364
    private String seller_id;

    // 交易目前所处的状态。详见下文 交易状态说明。示例值：TRADE_CLOSED
    private String trade_status;

    // 本次交易支付的订单金额，单位为人民币（元）。示例值：20
    private String total_amount;

    // 商家在交易中实际收到的款项，单位为人民币（元）。示例值：15
    private String receipt_amount;

    // 用户在交易中支付的金额。示例值：13.88
    private String buyer_pay_amount;

    // 退款通知中，返回总退款金额，单位为人民币（元），支持两位小数。示例值：2.58
    private String refund_fee;

    // 商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来。示例值：当面付交易
    private String subject;

    // 该订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来。示例值：当面付交易内容
    private String body;

    // 该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss
    private String gmt_create;

    // 该笔交易的买家付款时间。
    private String gmt_payment;

    // 该笔交易的退款时间
    private String gmt_refund;

    // 该笔交易结束时间
    private String gmt_close;

    // 公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝。
    // 示例值：merchantBizType%3d3C%26merchantBizNo%3d2016010101111
    private String passback_params;

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_biz_no() {
        return out_biz_no;
    }

    public void setOut_biz_no(String out_biz_no) {
        this.out_biz_no = out_biz_no;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_logon_id() {
        return buyer_logon_id;
    }

    public void setBuyer_logon_id(String buyer_logon_id) {
        this.buyer_logon_id = buyer_logon_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public String getBuyer_pay_amount() {
        return buyer_pay_amount;
    }

    public void setBuyer_pay_amount(String buyer_pay_amount) {
        this.buyer_pay_amount = buyer_pay_amount;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(String gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public String getGmt_refund() {
        return gmt_refund;
    }

    public void setGmt_refund(String gmt_refund) {
        this.gmt_refund = gmt_refund;
    }

    public String getGmt_close() {
        return gmt_close;
    }

    public void setGmt_close(String gmt_close) {
        this.gmt_close = gmt_close;
    }

    public String getPassback_params() {
        return passback_params;
    }

    public void setPassback_params(String passback_params) {
        this.passback_params = passback_params;
    }

    @Override
    public String toString() {
        return "AlipayNotifyMessage [notify_time=" + notify_time + ", notify_type=" + notify_type + ", notify_id="
                + notify_id + ", app_id=" + app_id + ", charset=" + charset + ", version=" + version + ", sign_type="
                + sign_type + ", sign=" + sign + ", trade_no=" + trade_no + ", out_trade_no=" + out_trade_no
                + ", out_biz_no=" + out_biz_no + ", buyer_id=" + buyer_id + ", buyer_logon_id=" + buyer_logon_id
                + ", seller_id=" + seller_id + ", trade_status=" + trade_status + ", total_amount=" + total_amount
                + ", receipt_amount=" + receipt_amount + ", buyer_pay_amount=" + buyer_pay_amount + ", refund_fee="
                + refund_fee + ", subject=" + subject + ", body=" + body + ", gmt_create=" + gmt_create
                + ", gmt_payment=" + gmt_payment + ", gmt_refund=" + gmt_refund + ", gmt_close=" + gmt_close
                + ", passback_params=" + passback_params + "]";
    }

}
