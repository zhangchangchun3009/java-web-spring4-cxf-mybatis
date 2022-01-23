package pers.zcc.scm.web.pay.wechatpay.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatNotifyMessage {
    // 通知的唯一ID
    private String id;
    // 通知创建的时间
    private String create_time;
    // 通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS
    private String event_type;
    // 通知的资源数据类型，支付成功通知为encrypt-resource
    private String resource_type;
    // 回调摘要 示例值：支付成功
    private String summary;
    // 通知资源数据
    private Resource resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "WechatNotifyMessage [id=" + id + ", create_time=" + create_time + ", event_type=" + event_type
                + ", resource_type=" + resource_type + ", summary=" + summary + ", resource=" + resource.toString()
                + "]";
    }

}
