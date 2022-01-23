package pers.zcc.scm.web.pay.alipay.config;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

@Named
public class AlipayConfig {
    // 商户appid
    @Value("${alipay.config.appid}")
    public String appid;

    // 商户私钥 pkcs8格式的
    @Value("${alipay.config.rsaPrivateKey}")
    public String rsaPrivateKey;

    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${alipay.config.notifyUrl}")
    public String notifyUrl;

    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 商户可以自定义同步跳转地址
    @Value("${alipay.config.returnUrl}")
    public String returnUrl;

    // 请求网关地址
    @Value("${alipay.config.url}")
    public String url;

    // 编码
    @Value("${alipay.config.charset}")
    public String charset;

    // 返回格式
    @Value("${alipay.config.format}")
    public String format;

    // 商户支付宝公钥
    @Value("${alipay.config.alipayPublicKey}")
    public String alipayPublicKey;

    // RSA2
    @Value("${alipay.config.signType}")
    public String signType;

    // 签约时的产品编码
    @Value("${alipay.config.productCode}")
    public String productCode;

    // 订单支付超时时间 m,h,只能整数
    @Value("${alipay.config.timeoutExpress}")
    public String timeoutExpress;

    // 取消支付返回页面
    @Value("${alipay.config.quitUrl}")
    public String quitUrl;

    /**
     * 2088开头的商户id 签约商家登录支付宝官网
     * (www.alipay.com)进入商家中心，点击页面上方的“我的商家服务”—“账户管理”，进入账户管理页面。
     * 在账户管理页面的“商户信息管理”，点击“查看PID|KEY”，即可查询到合作者身份uid，uid是以2088开头的16位纯数字。
     */

    @Value("${alipay.config.sellerId}")
    public String sellerId;

    // ISV 代理商户获取的第三方商户应用授权口令
    @Value("${alipay.config.appAuthToken}")
    public String appAuthToken;

}