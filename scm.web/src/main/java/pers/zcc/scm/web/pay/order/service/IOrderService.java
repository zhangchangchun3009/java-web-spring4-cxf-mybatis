
package pers.zcc.scm.web.pay.order.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.github.pagehelper.PageInfo;

import pers.zcc.scm.common.vo.PageVO;
import pers.zcc.scm.common.vo.Response;
import pers.zcc.scm.web.pay.order.OrderVO;

/**
 * 订单服务
 *
 * @author zhangchangchun
 * @since 2021年5月13日
 */
@Produces("application/json")
@Path("/orderService")
public interface IOrderService {

    /**
     * 生成订单
     *
     * @param paramVo the plate num
     * @return the response 
     */
    @POST
    @Path("/getOrder")
    Response<OrderVO> generateOrder();

    /**
     * Preview bill. 预览账单
     *
     * @return the response 费用在previewBill字段
     */
    @POST
    @Path("/previewBill")
    Response<String> previewBill();

    /**
     * Find order.
     *
     * @param orderParam the order param
     * @return the order VO
     */
    OrderVO findOrder(OrderVO orderParam);

    /**
     * Update.
     *
     * @param order the order
     */
    void updateBeforeClose(OrderVO order);

    void update(OrderVO order);

    /**
     * Gets the orders.
     *
     * @param param  the param
     * @param pageVo the page vo
     * @return the orders
     */
    @GET
    @Path("/getOrders/page/{pageSize}/{pageNum}")
    PageInfo<OrderVO> getOrders(@QueryParam("") OrderVO param, @PathParam("") PageVO pageVo);

}
