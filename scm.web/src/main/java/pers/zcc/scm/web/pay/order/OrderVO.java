package pers.zcc.scm.web.pay.order;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

public class OrderVO {
    private Long id;

    /**
     * 订单交易号
     */
    private String tradeNo;

    /**
     * 订单创建时间
     */
    private String createdTime;

    /**
     * 向支付平台下单时间
     */
    private String startPayTime;

    /**
     * 订单费用
     */
    private BigDecimal bill;

    /**
     * 订单状态.0新建待支付；9，未支付关闭；8，正常支付成功关闭;7
     */
    private Integer status;

    /**
     * 订单费用类型。
     */
    private Integer type;

    /**
     * 订单关闭时间
     */
    private String closeTime;

    /**
     * 订单描述
     */
    private String description;

    /**
     * 是否处理过微信通知
     */
    @JsonIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Integer wechatNotified;

    /**
     * 是否处理过阿里通知
     */
    @JsonIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Integer alipayNotified;

    /**
     * 交易渠道。0，现金；1，支付宝；2，微信H5；3，微信JS；
     */
    private Integer channel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getStartPayTime() {
        return startPayTime;
    }

    public void setStartPayTime(String startPayTime) {
        this.startPayTime = startPayTime;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the wechatNotified
     */
    public Integer getWechatNotified() {
        return wechatNotified;
    }

    /**
     * @param wechatNotified the wechatNotified to set
     */
    public void setWechatNotified(Integer wechatNotified) {
        this.wechatNotified = wechatNotified;
    }

    /**
     * @return the alipayNotified
     */
    public Integer getAlipayNotified() {
        return alipayNotified;
    }

    /**
     * @param alipayNotified the alipayNotified to set
     */
    public void setAlipayNotified(Integer alipayNotified) {
        this.alipayNotified = alipayNotified;
    }

    /**
     * get 交易渠道。0，现金；1，支付宝；2，微信H5；3，微信JS； .
     * @return channel
     */
    public Integer getChannel() {
        return channel;
    }

    /**
     * set 交易渠道。0，现金；1，支付宝；2，微信H5；3，微信JS；.
     * @param channel
     */
    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
