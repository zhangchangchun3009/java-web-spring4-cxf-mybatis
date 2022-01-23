package pers.zcc.scm.web.pay.wechatpay.vo;

public class Payer {
    // 用户在服务商appid下的唯一标识。
    private String sp_openid;
    // 用户在子商户appid下的唯一标识。
    private String sub_openid;
    /**
     * @return the sp_openid
     */
    public String getSp_openid() {
        return sp_openid;
    }
    /**
     * @param sp_openid the sp_openid to set
     */
    public void setSp_openid(String sp_openid) {
        this.sp_openid = sp_openid;
    }
    /**
     * @return the sub_openid
     */
    public String getSub_openid() {
        return sub_openid;
    }
    /**
     * @param sub_openid the sub_openid to set
     */
    public void setSub_openid(String sub_openid) {
        this.sub_openid = sub_openid;
    }

}
