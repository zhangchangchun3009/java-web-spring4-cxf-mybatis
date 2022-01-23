package pers.zcc.scm.web.pay.wechatpay.service;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pers.zcc.scm.common.vo.Response;
import pers.zcc.scm.web.pay.order.OrderVO;
import pers.zcc.scm.web.pay.wechatpay.vo.PaymentVO;

@Produces("application/json")
@Path("/wechatpayService")
public interface IWechatpayService {

    /**
     * Pay.（其實只是生成了微信内部訂單，支付還需前端流程完成）
     *
     * @param paymentVO the payment VO
     * @return 返回的h5支付链接。前端需在链接拼上return_url作为支付成功回调地址
     */
    Response<String> h5Pay(PaymentVO paymentVO);

    /**
     * Js api pay. jsapi支付方式下单，返回的是prepayid，24小时有效
     *
     * @param paymentVO the payment VO
     * @return the response
     */
    Response<String> jsApiPay(PaymentVO paymentVO);

    /**
     * Gets the sp openid. 通过code换取accesstoken,最终换取openid
     *
     * @param code the code
     * @return the sp openid
     */
    Response<String> getSpOpenid(String code);

    /**
     * Query order status.支付完成回调页面前端查询订单状态确定是否支付成功
     *
     * @param order the order 订单详情
     * @return the string 订单状态
     */
    Integer queryOrderStatus(OrderVO order);
}
