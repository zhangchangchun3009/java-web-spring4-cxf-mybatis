package pers.zcc.scm.web.pay.alipay.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;

import pers.zcc.scm.common.vo.Response;
import pers.zcc.scm.web.pay.alipay.config.AlipayConfig;
import pers.zcc.scm.web.pay.alipay.vo.PaymentVO;
import pers.zcc.scm.web.pay.order.OrderStatusEnum;
import pers.zcc.scm.web.pay.order.OrderVO;

@Named
public class AlipayService implements IAlipayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayService.class);

    @Inject
    private AlipayConfig config;

    @Override
    public Response<String> pay(HttpServletResponse response, PaymentVO paymentVO) {
        Response<String> result = new Response<String>();
        String outTradeNo = paymentVO.getOutTradeNo();
        AlipayClient client = getPayClient();
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        // 调用RSA签名方式
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        String subject = paymentVO.getSubject();
        String totalAmount = paymentVO.getTotalAmount();
        String body = paymentVO.getBody();
        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(outTradeNo);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setBody(body);
        model.setTimeoutExpress(config.timeoutExpress);
        model.setProductCode(config.productCode);
        model.setQuitUrl(config.quitUrl);
        alipayRequest.setBizModel(model);
        // 设置异步通知地址
        alipayRequest.setNotifyUrl(config.notifyUrl);
        // 设置同步地址
        alipayRequest.setReturnUrl(config.returnUrl);
        try {
            // 调用SDK生成表单
            AlipayTradeWapPayResponse payResponse = client.pageExecute(alipayRequest);
            if (payResponse.isSuccess()) {
                // form表单生成
                String form = payResponse.getBody();
                response.setContentType("text/html;charset=" + config.charset);
                ServletOutputStream out = response.getOutputStream();
                out.write(form.getBytes("UTF-8"));// 直接将完整的表单html输出到页面
                return result.success();
            } else {
                LOGGER.error("alipay e," + payResponse.getMsg());
                return result.fail("010", "阿里支付接口应答失败." + payResponse.getMsg());
            }
        } catch (Exception e) {
            LOGGER.error("alipay e,", e);
            return result.fail("011", "阿里支付接口异常,异常原因" + e.getMessage());
        }
    }

    private AlipayClient getPayClient() {
        AlipayClient client = new DefaultAlipayClient(config.url, config.appid, config.rsaPrivateKey, config.format,
                config.charset, config.alipayPublicKey, config.signType);
        return client;
    }

    @Override
    public Integer queryOrderStatus(OrderVO order) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
        bizModel.setOutTradeNo(order.getTradeNo());
        request.setBizModel(bizModel);
//        request.putOtherTextParam("app_auth_token", config.appAuthToken);
        Integer orderStatus = OrderStatusEnum.NOT_NOTIFIED.getValue();
        try {
            AlipayClient client = getPayClient();
            AlipayTradeQueryResponse response = client.execute(request);
            if (response.isSuccess()) {
                String tradeNo = response.getOutTradeNo();
                String tradeStatus = response.getTradeStatus();
                if (tradeNo.equals(order.getTradeNo()) && ("TRADE_FINISHED".equalsIgnoreCase(tradeStatus)
                        || "TRADE_SUCCESS".equalsIgnoreCase(tradeStatus))) {
                    orderStatus = OrderStatusEnum.PAY_SUCCESS.getValue();
                }
            }
        } catch (AlipayApiException e) {
            LOGGER.error("AlipayTradeQueryRequest e,", e);
        }
        return orderStatus;
    }

}
