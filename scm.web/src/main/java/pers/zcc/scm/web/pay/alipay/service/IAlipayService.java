package pers.zcc.scm.web.pay.alipay.service;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pers.zcc.scm.common.vo.Response;
import pers.zcc.scm.web.pay.alipay.vo.PaymentVO;
import pers.zcc.scm.web.pay.order.OrderVO;

/**
 * The Interface IAlipayService.
 *
 * @author zhangchangchun
 * @since 2021年5月18日
 */
@Produces("application/json")
@Path("/alipayService")
public interface IAlipayService {

    /**
     * Pay.（其實只是生成了支付寶訂單，支付還需前端完成）
     *
     * @param response the response
     * @param paymentVO the payment VO
     * @return true, if successful response 返回的支付訂單表單頁面
     */
    Response<String> pay(HttpServletResponse response, PaymentVO paymentVO);

    /**
     * Query order status.支付完成回调页面前端查询订单状态确定是否支付成功
     *
     * @param order the order 订单详情
     * @return the string 订单状态
     */
    Integer queryOrderStatus(OrderVO order);
}
