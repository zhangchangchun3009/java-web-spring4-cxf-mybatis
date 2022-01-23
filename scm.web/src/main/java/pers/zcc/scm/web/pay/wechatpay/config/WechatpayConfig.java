package pers.zcc.scm.web.pay.wechatpay.config;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

@Named
public class WechatpayConfig {

    /**
     * 商户私钥
     */
    @Value("${wechatpay.config.privateKey}")
    public String privateKey;

    /**
     * 商户号
     */
    @Value("${wechatpay.config.merchantId}")
    public String merchantId;

    /**
     * 商户证书的证书序列号
     */
    @Value("${wechatpay.config.merchantSerialNumber}")
    public String merchantSerialNumber;

    /**
     * 登录微信服务商平台，进入【账户中心 > API安全 > API安全】目录，点击【设置密钥】。
     */
    @Value("${wechatpay.config.apiV3Key}")
    public String apiV3Key;

    /**
     * 服务商申请的公众号appid。 示例值：wx8888888888888888
     */
    @Value("${wechatpay.config.sp_appid}")
    public String sp_appid;

    @Value("${wechatpay.config.appsecret}")
    public String sp_appsecret;

    /**
     * 服务商户号，由微信支付生成并下发 示例值：1230000109
     */
    @Value("${wechatpay.config.sp_mchid}")
    public String sp_mchid;

    /**
     * 子商户的商户号，由微信支付生成并下发。 示例值：1900000109
     */
    @Value("${wechatpay.config.sub_mchid}")
    public String sub_mchid;

    /**
     * 超时时长，秒
     */
    @Value("${wechatpay.config.time_expire}")
    public int time_expire;

    /**
     * 通知URL必须为直接可访问的URL，不允许携带查询串。 格式：URL ,https的
     */
    @Value("${wechatpay.config.notify_url}")
    public String notify_url;

    /**
     * h5支付下单接口
     */
    @Value("${wechatpay.config.h5pay_url}")
    public String h5pay_url;

    /**
     * 根据商户订单号查询支付结果url
     */
    @Value("${wechatpay.config.order_query_url}")
    public String order_query_url;

    @Value("${wechatpay.config.openid_url}")
    public String openid_url;

    @Value("${wechatpay.config.jsapipay_url}")
    public String jsapipay_url;

}
