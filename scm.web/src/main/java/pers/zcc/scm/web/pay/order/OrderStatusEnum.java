package pers.zcc.scm.web.pay.order;

/**
 * 订单状态
 *
 * @author zhangchangchun
 * @since 2021年4月27日
 */
public enum OrderStatusEnum {

    UNPAID(0), NOT_NOTIFIED(7), PAY_SUCCESS(8), PAY_FAILED(9);

    private int value;

    OrderStatusEnum(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
