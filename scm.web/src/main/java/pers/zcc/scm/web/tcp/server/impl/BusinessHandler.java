package pers.zcc.scm.web.tcp.server.impl;

import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.util.RSAUtil;
import pers.zcc.scm.web.tcp.protocol.TcpCommandEnums;
import pers.zcc.scm.web.tcp.server.interfaces.ITcpServerReceiver;

@ChannelHandler.Sharable
@Named
public class BusinessHandler extends ChannelInboundHandlerAdapter {
    private final static Logger log = LoggerFactory.getLogger(BusinessHandler.class);

    private static Charset charset = Charset.forName("UTF-8");

    ObjectMapper mapper = JacksonUtil.getDefaultObjectMapper();

    @Inject
    MessageCacheService cacheService;

    @Inject
    ITcpServerReceiver tcpReceiver;

    @Inject
    WriteCommandService writeService;

    @Value("${tcp.client.publicKey}")
    private String clientPublicKey;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null)
            return;
        try {
            String json = msg.toString();
            if (json.length() < 2)
                return;
            System.out.println("接收到的原始数据：" + json);
            JsonNode node = mapper.readTree(json);

            String unitId = node.get("unitId").asText();
            int command = Integer.parseInt(node.get("command").asText());
            String messageId = node.get("messageId").asText();
            String content = node.get("content").asText();
            long timestamp = Long.parseLong(node.get("timestamp").asText());
            String sign = node.get("sign").asText();
            //进行json验签
            String data = "&" + unitId + "&" + command + "&" + messageId + "&" + content + "&" + timestamp + "&";
            long now = System.currentTimeMillis();
            boolean pass = false;
            try {
                pass = (now - timestamp) < 5 * 60000 && RSAUtil.verify(data.getBytes(charset), clientPublicKey, sign);
            } catch (Exception e) {
            }
            if (!pass) {
                log.info("验签失败!");
                return;
            }
            if (command == TcpCommandEnums.LOGIN.getValue()) {
                this.doLogin(ctx, unitId, command, content);
            } else {
                if (cacheService.getChannel(unitId) == null)
                    return;
                tcpReceiver.read(unitId, command, messageId, content);
            }

        } catch (Exception e) {
            System.out.println("错误的Tcp数据包格式，数据解析异常！");
        }
    }

    private void doLogin(ChannelHandlerContext ctx, String unitId, int command, String content) throws Exception {
        log.info("Tcp服务器接收到数据 : unitId={},command={},content={}", unitId, command, content);
        //判断当前连接是否已经登录，如果没有，判断当前请求是否为登录请求，否则丢弃；

        if (tcpReceiver.login(unitId, content)) {
            //如果登录成功
            cacheService.put(unitId, ctx.channel());
            log.info("登录成功！");
            //需要往客户端返回登录成功信息；
            writeService.writeCommand(unitId, TcpCommandEnums.LOGIN.getValue(), "", "success");
        } else {
            log.info("登录失败！");
            //非法的数据包信息
            //ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("已经5秒未收到客户端的消息了！");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                cacheService.remove(ctx.channel());
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
