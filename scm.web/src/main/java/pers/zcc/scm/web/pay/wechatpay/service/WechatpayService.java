package pers.zcc.scm.web.pay.wechatpay.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.util.RSAUtil;
import pers.zcc.scm.common.vo.Response;
import pers.zcc.scm.web.pay.order.OrderStatusEnum;
import pers.zcc.scm.web.pay.order.OrderVO;
import pers.zcc.scm.web.pay.wechatpay.config.WechatHttpClientUtil;
import pers.zcc.scm.web.pay.wechatpay.config.WechatpayConfig;
import pers.zcc.scm.web.pay.wechatpay.vo.PaymentVO;

@Named
public class WechatpayService implements IWechatpayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatpayService.class);

    @Inject
    private WechatpayConfig config;

    @Override
    public Response<String> h5Pay(PaymentVO paymentVO) {
        Response<String> response = new Response<String>();
        HttpPost httpPost = new HttpPost(config.h5pay_url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        int time_expire = config.time_expire;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timeoutTime = sdf.format(new Date(System.currentTimeMillis() + time_expire * 1000));
        String subMchid = config.sub_mchid;
        objectNode.put("sp_appid", config.sp_appid).put("sp_mchid", config.sp_mchid).put("sub_mchid", subMchid)
                .put("description", paymentVO.getDescription()).put("out_trade_no", paymentVO.getOutTradeNo())
                .put("time_expire", timeoutTime).put("notify_url", config.notify_url);
        objectNode.putObject("amount").put("total", Integer.parseInt(paymentVO.getTotal()));
        ObjectNode sceneInfoNode = objectNode.putObject("scene_info");
        sceneInfoNode.put("payer_client_ip", paymentVO.getPayerClientIp());
        sceneInfoNode.putObject("h5_info").put("type", "Wap");
        String reqdata = null;
        try {
            reqdata = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            LOGGER.error("wechat pay json serialize e,", e);
            return response.fail("020", e.getMessage());
        }
        CloseableHttpResponse wechatResponse = null;
        try {
            httpPost.setEntity(new StringEntity(reqdata, "UTF-8"));
            CloseableHttpClient httpClient = WechatHttpClientUtil.getInstance();
            wechatResponse = httpClient.execute(httpPost);
            int statusCode = wechatResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) { // 处理成功
                return response.success(EntityUtils.toString(wechatResponse.getEntity()));
            } else {
                String body = EntityUtils.toString(wechatResponse.getEntity());
                LOGGER.debug("wechat pay failed,response:" + body);
                return response.fail("021", "failed,resp code = " + statusCode + ",return body = " + body);
            }
        } catch (Exception e) {
            LOGGER.error("wechat h5 pay http fail,", e);
            return response.fail("022", e.getMessage());
        } finally {
            try {
                if (wechatResponse != null) {
                    EntityUtils.consume(wechatResponse.getEntity());
                }
            } catch (Exception e) {
                LOGGER.error("EntityUtils.consume e,", e);
            }
        }
    }

    @Override
    public Integer queryOrderStatus(OrderVO order) {
        Integer orderStatus = OrderStatusEnum.NOT_NOTIFIED.getValue();
        CloseableHttpClient httpClient = WechatHttpClientUtil.getInstance();
        String subMerChantId = config.sub_mchid;
        HttpGet getRequest = new HttpGet(config.order_query_url + order.getTradeNo() + "?sp_mchid=" + config.sp_mchid
                + "&sub_mchid=" + subMerChantId);
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-type", "application/json; charset=utf-8");
        CloseableHttpResponse orderResponse = null;
        try {
            String jsonResult = null;
            orderResponse = httpClient.execute(getRequest);
            int httpCode = orderResponse.getStatusLine().getStatusCode();
            if (httpCode == 200) {
                jsonResult = EntityUtils.toString(orderResponse.getEntity());
            } else {
                LOGGER.error("httpCode=" + httpCode + ",message=" + EntityUtils.toString(orderResponse.getEntity()));
            }
            if (!StringUtils.isEmpty(jsonResult)) {
                LOGGER.debug("jsonResult:" + jsonResult);
                ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
                JsonNode root = objectMapper.readTree(jsonResult);
                JsonNode trade_state_node = root.findValue("trade_state");
                if (trade_state_node != null) {
                    String trade_state = trade_state_node.asText();
                    if ("SUCCESS".equalsIgnoreCase(trade_state)) {
                        orderStatus = OrderStatusEnum.PAY_SUCCESS.getValue();
                    }
                }
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        } finally {
            if (orderResponse != null) {
                try {
                    EntityUtils.consume(orderResponse.getEntity());
                } catch (IOException e) {
                    LOGGER.error("IOException", e);
                }
            }
        }
        return orderStatus;
    }

    @Override
    public Response<String> jsApiPay(PaymentVO paymentVO) {
        Response<String> response = new Response<String>().success();
        Response<String> openidResponse = getSpOpenid(paymentVO.getCode());
        if (!openidResponse.exeSuccess()) {
            return openidResponse;
        }
        String openid = openidResponse.getData();
        HttpPost httpPost = new HttpPost(config.jsapipay_url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        int time_expire = config.time_expire;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timeoutTime = sdf.format(new Date(System.currentTimeMillis() + time_expire * 1000));
        String subMchid = config.sub_mchid;
        objectNode.put("sp_appid", config.sp_appid).put("sp_mchid", config.sp_mchid).put("sub_mchid", subMchid)
                .put("description", paymentVO.getDescription()).put("out_trade_no", paymentVO.getOutTradeNo())
                .put("time_expire", timeoutTime).put("notify_url", config.notify_url);
        objectNode.putObject("amount").put("total", Integer.parseInt(paymentVO.getTotal()));
        objectNode.putObject("payer").put("sp_openid", openid);
        String reqdata = null;
        try {
            reqdata = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            LOGGER.error("wechat jsapipay json serialize e,", e);
            return response.fail("020", e.getMessage());
        }
        CloseableHttpResponse wechatResponse = null;
        try {
            httpPost.setEntity(new StringEntity(reqdata, "UTF-8"));
            CloseableHttpClient httpClient = WechatHttpClientUtil.getInstance();
            wechatResponse = httpClient.execute(httpPost);
            int statusCode = wechatResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) { // 处理成功
                String prepayidJson = EntityUtils.toString(wechatResponse.getEntity());
                String prepayid = objectMapper.readTree(prepayidJson).findValue("prepay_id").asText();
                ObjectNode data = objectMapper.createObjectNode();
                String appId = config.sp_appid;
                long timeStamp = System.currentTimeMillis();
                String nonceStr = RSAUtil.getPublicKey(RSAUtil.genKeyPair()).substring(0, 32);
                String body = "prepay_id=" + prepayid;
                String paySign = WechatHttpClientUtil.sign(appId, timeStamp, nonceStr, body);
                data.put("appId", appId);
                data.put("timeStamp", timeStamp);
                data.put("nonceStr", nonceStr);
                data.put("package", body);
                data.put("signType", "RSA");
                data.put("paySign", paySign);
                return response.success(objectMapper.writeValueAsString(data));
            } else {
                String body = EntityUtils.toString(wechatResponse.getEntity());
                LOGGER.debug("wechat jsapipay failed,response:" + body);
                return response.fail("021", "failed,resp code = " + statusCode + ",return body = " + body);
            }
        } catch (Exception e) {
            LOGGER.error("wechat jsapipay http fail,", e);
            return response.fail("022", e.getMessage());
        } finally {
            try {
                if (wechatResponse != null) {
                    EntityUtils.consume(wechatResponse.getEntity());
                }
            } catch (Exception e) {
                LOGGER.error("EntityUtils.consume e,", e);
            }
        }
    }

    @Override
    public Response<String> getSpOpenid(String code) {
        Response<String> response = new Response<String>().success();
        if (code == null || code.equals("")) {
            return response.fail("001", "code is null");
        }
        HttpGet getRequest = new HttpGet(config.openid_url + "?appid=" + config.sp_appid + "&secret="
                + config.sp_appsecret + "&code=" + code + "&grant_type=authorization_code");
        CloseableHttpClient httpClient = WechatHttpClientUtil.getInstance();
        CloseableHttpResponse openidResponse = null;
        try {
            openidResponse = httpClient.execute(getRequest);
            String jsonResult = null;
            int httpCode = openidResponse.getStatusLine().getStatusCode();
            if (httpCode == 200) {
                jsonResult = EntityUtils.toString(openidResponse.getEntity());
            } else {
                LOGGER.error("httpCode=" + httpCode + ",message=" + EntityUtils.toString(openidResponse.getEntity()));
            }
            if (!StringUtils.isEmpty(jsonResult)) {
                LOGGER.debug("jsonResult:" + jsonResult);
                ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
                JsonNode root = objectMapper.readTree(jsonResult);
                JsonNode openid = root.findValue("openid");
                if (openid != null) {
                    return response.success(openid.asText());
                } else {
                    JsonNode errcode = root.findValue("errcode");
                    JsonNode errmsg = root.findValue("errmsg");
                    String message = "calling wechat get openid api failed,errcode=" + errcode.asText() + ",errmsg="
                            + errmsg.asText();
                    LOGGER.error(message);
                    return response.fail("031", message);
                }
            } else {
                return response.fail("032", "calling wechat get openid api failed,return null");
            }
        } catch (IOException e) {
            LOGGER.error("IOException", e);
            return response.fail("021", "calling wechat get openid api occurs exception");
        } finally {
            if (openidResponse != null) {
                try {
                    EntityUtils.consume(openidResponse.getEntity());
                } catch (IOException e) {
                    LOGGER.error("IOException", e);
                }
            }
        }
    }
}
