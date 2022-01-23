package pers.zcc.scm.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;

import pers.zcc.scm.common.util.ApplicationContextManager;
import pers.zcc.scm.common.util.HttpContextManager;
import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.web.pay.order.OrderStatusEnum;
import pers.zcc.scm.web.pay.order.OrderVO;
import pers.zcc.scm.web.pay.order.service.IOrderService;
import pers.zcc.scm.web.pay.wechatpay.config.WechatHttpClientUtil;
import pers.zcc.scm.web.pay.wechatpay.config.WechatpayConfig;
import pers.zcc.scm.web.pay.wechatpay.vo.Resource;
import pers.zcc.scm.web.pay.wechatpay.vo.WechatNotifyMessage;
import pers.zcc.scm.web.pay.wechatpay.vo.WechatNotifyResource;
import pers.zcc.scm.web.pay.wechatpay.vo.WechatNotifyResponse;

public class WechatpayNotifyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatpayNotifyServlet.class);

    private WechatpayConfig config;

    private IOrderService orderService;

    @Override
    public void init() throws ServletException {
        config = (WechatpayConfig) ApplicationContextManager.getBean("wechatpayConfig");
        orderService = (IOrderService) ApplicationContextManager.getBean("orderService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = HttpContextManager.getRequestBody(req);
        if (body == null) {
            LOGGER.error("微信回调消息为空");
            responseStr(resp);
            return;
        }
        boolean headerVerify = false;
        try {
            headerVerify = WechatHttpClientUtil.verifyWechatRequestSign(req, body);
        } catch (Exception e) {
            LOGGER.error("微信回调验签异常，", e);
            responseStr(resp);
            return;
        }
        if (!headerVerify) {
            LOGGER.error("微信回调验签不通过," + body);
            responseStr(resp);
            return;
        }
        WechatNotifyMessage message = JacksonUtil.getObjectMapper().readValue(body, WechatNotifyMessage.class);
        if ("TRANSACTION.SUCCESS".equals(message.getEvent_type())) {
            try {
                Resource resource = message.getResource();
                String ciphertext = resource.getCiphertext();
                String nonce = resource.getNonce();
                String associated_data = resource.getAssociated_data();
                AesUtil aesUtil = new AesUtil(config.apiV3Key.getBytes("UTF-8"));
                String payInfo = aesUtil.decryptToString(
                        associated_data == null ? new byte[0] : associated_data.getBytes("UTF-8"),
                        nonce.getBytes("UTF-8"), ciphertext);
                if (!StringUtils.isEmpty(payInfo)) {
                    WechatNotifyResource decryptedResource = JacksonUtil.getObjectMapper().readValue(payInfo,
                            WechatNotifyResource.class);
                    String tradeNo = decryptedResource.getOut_trade_no();
                    OrderVO orderParam = new OrderVO();
                    orderParam.setTradeNo(tradeNo);
                    OrderVO order = orderService.findOrder(orderParam);
                    if (order.getWechatNotified() == 1
                            || order.getStatus().intValue() == OrderStatusEnum.PAY_SUCCESS.getValue()) {
                        responseStr(resp);
                        return;
                    }
                    order.setWechatNotified(1);
                    String state = decryptedResource.getTrade_state();
                    if ("SUCCESS".equalsIgnoreCase(state)) {
                        order.setStatus(OrderStatusEnum.PAY_SUCCESS.getValue());
                    } else {
                        order.setStatus(OrderStatusEnum.PAY_FAILED.getValue());
                    }
                    orderService.updateBeforeClose(order);
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("UnsupportedEncodingException", e);
            } catch (GeneralSecurityException e) {
                LOGGER.error("GeneralSecurityException", e);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }
        }
        responseStr(resp);
        return;
    }

    private void responseStr(HttpServletResponse resp) {
        try {
            WechatNotifyResponse response = new WechatNotifyResponse();
            response.setCode("SUCCESS");
            response.setMessage("成功");
            resp.getOutputStream().write(JacksonUtil.getObjectMapper().writeValueAsString(response).getBytes("UTF-8"));
        } catch (Exception e) {
            LOGGER.error("WechatpayNotify e,", e);
        }
    }

}
