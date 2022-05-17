package pers.zcc.scm.web.tcp.server.impl;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.util.RSAUtil;

@Named
public class WriteCommandService {
    ObjectMapper mapper = JacksonUtil.getDefaultObjectMapper();

    private Charset charset = Charset.forName("UTF-8");

    @Value("${tcp.server.privateKey}")
    private String serverPrivateKey;

    @Inject
    MessageCacheService cacheService;

    public int writeCommand(String unitId, int command, String messageId, Object content) {
        Channel channel = cacheService.getChannel(unitId);
        if (channel == null) {
            System.out.println("此应用没有连接到tcp服务端 " + command + "  " + content);
            return 1;
        }
        String contentJson = null;
        try {
            contentJson = mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            return 1;
        }
        return this.writeChannelCommand(channel, unitId, command, messageId, contentJson);
    }

    public int writeCommandWithJson(String unitId, int command, String messageId, String contentJson) {
        Channel channel = cacheService.getChannel(unitId);
        if (channel == null) {
            System.out.println("此应用没有连接到tcp服务端 " + command + "  " + contentJson);
            return 1;
        }
        return this.writeChannelCommand(channel, unitId, command, messageId, contentJson);
    }

    private int writeChannelCommand(Channel channel, String unitId, int command, String messageId, String contentJson) {
        long timestamp = System.currentTimeMillis();
        String data = "&" + command + "&" + messageId + "&" + contentJson + "&" + timestamp + "&";
        String sign = null;
        try {
            sign = RSAUtil.sign(data.getBytes(charset), serverPrivateKey);
        } catch (Exception e) {
            return 0;
        }

        Map<Object, Object> toJson = new HashMap<>();
        toJson.put("command", command);
        toJson.put("messageId", messageId);
        toJson.put("content", contentJson);
        toJson.put("timestamp", timestamp);
        toJson.put("sign", sign);

        try {
            return this.writeChannelJson(channel, mapper.writeValueAsString(toJson));
        } catch (JsonProcessingException e) {
            return 1;
        }
    }

    private int writeChannelJson(Channel channel, String json) {
        try {
            json = json + "\r\n";
            byte[] bytes = json.getBytes(charset);
            ByteBuf buf = Unpooled.buffer(bytes.length);
            buf.writeBytes(bytes);
            channel.writeAndFlush(buf);
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
}
