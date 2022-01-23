package pers.zcc.scm.web.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.internal.util.AlipaySignature;

import pers.zcc.scm.common.util.ApplicationContextManager;
import pers.zcc.scm.web.pay.alipay.config.AlipayConfig;
import pers.zcc.scm.web.pay.order.OrderStatusEnum;
import pers.zcc.scm.web.pay.order.OrderVO;
import pers.zcc.scm.web.pay.order.service.IOrderService;

public class AlipayNotifyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayNotifyServlet.class);

    private AlipayConfig config;

    private IOrderService orderService;

    @Override
    public void init() throws ServletException {
        config = (AlipayConfig) ApplicationContextManager.getBean("alipayConfig");
        orderService = (IOrderService) ApplicationContextManager.getBean("orderService");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                // valueStr = new String(valueStr.getBytes("ISO-8859-1"),
                // "gbk");
                params.put(name, valueStr);
            }
            LOGGER.debug("params:" + Collections.singletonList(params));
            // 未验签前一切数据存疑，因验签失败导致的结束不应返回success
            String tradeNo = params.get("out_trade_no");
            if (tradeNo == null || tradeNo.equals("")) {
                return;
            }

            boolean verify_result = AlipaySignature.rsaCheckV1(params, config.alipayPublicKey, config.charset,
                    config.signType);
            if (!verify_result) {
                LOGGER.debug("verify_result:" + verify_result);
                return;
            }
            OrderVO orderParam = new OrderVO();
            orderParam.setTradeNo(tradeNo);
            OrderVO order = orderService.findOrder(orderParam);
            if (order.getAlipayNotified() == 1
                    || order.getStatus().intValue() == OrderStatusEnum.PAY_SUCCESS.getValue()) {
                responseStr(resp);
                return;
            }
            String tradeStatus = params.get("trade_status");
            int oederStatus = OrderStatusEnum.PAY_FAILED.getValue();
            if ("TRADE_FINISHED".equalsIgnoreCase(tradeStatus) || "TRADE_SUCCESS".equalsIgnoreCase(tradeStatus)) {
                oederStatus = OrderStatusEnum.PAY_SUCCESS.getValue();
            }
            order.setStatus(oederStatus);
            order.setAlipayNotified(1);
            orderService.updateBeforeClose(order);
        } catch (Exception e) {
            LOGGER.error("alipayNotify e,", e);
        }
        responseStr(resp);
    }

    private void responseStr(HttpServletResponse resp) {
        try {
            resp.getOutputStream().write("success".getBytes("UTF-8"));
        } catch (Exception e) {
            LOGGER.error("alipayNotify e,", e);
        }
    }

}
