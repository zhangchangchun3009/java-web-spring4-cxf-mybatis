package pers.zcc.scm.web.pay.wechatpay.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import pers.zcc.scm.common.util.ApplicationContextManager;
import pers.zcc.scm.common.util.RSAUtil;

public class WechatHttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatHttpClientUtil.class);

    private static CloseableHttpClient wechatHttpClient;

    private static AutoUpdateCertificatesVerifier verifier;

    private static String privateKey;

    private static WechatpayConfig config;

    static {
        init();
    }

    public static void init() {
        try {
            config = ApplicationContextManager.getBean(WechatpayConfig.class);
            // 加载商户私钥（privateKey：私钥字符串）
            PrivateKey merchantPrivateKey = PemUtil
                    .loadPrivateKey(new ByteArrayInputStream(config.privateKey.getBytes("utf-8")));
            // 加载微信平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
            verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(config.merchantId,
                            new PrivateKeySigner(config.merchantSerialNumber, merchantPrivateKey)),
                    config.apiV3Key.getBytes("utf-8"));

            // 初始化httpClient
            wechatHttpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(config.merchantId, config.merchantSerialNumber, merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier))
                    .setDefaultSocketConfig(SocketConfig.custom().setSoKeepAlive(false).setSoTimeout(2000).build())
                    .setConnectionTimeToLive(60000, TimeUnit.MILLISECONDS).build();
            privateKey = config.privateKey;
        } catch (Exception e) {
            LOGGER.error("wechatHttpClient init e,", e);
        }
    }

    public static CloseableHttpClient getInstance() {
        return wechatHttpClient;
    }

    public static boolean verifyWechatRequestSign(HttpServletRequest request, String responseBody)
            throws ClientProtocolException, IOException {
        // 加载微信平台证书公钥
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String serialNumber = request.getHeader("Wechatpay-Serial");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String signature = request.getHeader("Wechatpay-Signature");
            String message = buildMessage(timestamp, nonce, responseBody);
            return validateParameters(timestamp, serialNumber, nonce, signature, responseBody)
                    && verifier.verify(serialNumber, message.getBytes("UTF-8"), signature);
        } catch (Exception e) {
            LOGGER.error("verifyWechatRequestSign e,", e);
            return false;
        }
    }

    private static String buildMessage(String timestamp, String nonce, String body) {
        return timestamp + "\n" + nonce + "\n" + body + "\n";
    }

    private static boolean validateParameters(String timestamp, String serialNumber, String nonce, String signature,
            String responseBody) {
        return !(StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(serialNumber) || StringUtils.isEmpty(nonce)
                || StringUtils.isEmpty(signature) || StringUtils.isEmpty(responseBody));
    }

    public static String sign(String appId, long timestamp, String nonce, String body) {
        String content = appId + "\n" + timestamp + "\n" + nonce + "\n" + body + "\n";
        try {
            return RSAUtil.sign(content.getBytes("UTF-8"), privateKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
