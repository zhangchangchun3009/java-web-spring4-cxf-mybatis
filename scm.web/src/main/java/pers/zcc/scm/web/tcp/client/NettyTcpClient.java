
package pers.zcc.scm.web.tcp.client;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import pers.zcc.scm.common.util.EnvironmentProps;
import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.util.RSAUtil;
import pers.zcc.scm.web.tcp.client.impl.TcpServerConfig;
import pers.zcc.scm.web.tcp.client.interfaces.ITcpClientReceiver;
import pers.zcc.scm.web.tcp.protocol.TcpCommandEnums;

@Named
public class NettyTcpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyTcpClient.class);

    @Inject
    private TcpServerConfig serverInfo;

    @Inject
    private ITcpClientReceiver tcpReceiver;

    private Channel channel;

    private boolean isLogin = false;

    private boolean exit = false;

    private static ObjectMapper mapper = JacksonUtil.getObjectMapper();

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    private static Charset charset = Charset.forName("UTF-8");

    private static String serverPublicKey = EnvironmentProps.getApplicationProp("tcp.server.publicKey");

    private static final byte[] HEARTBEAT = new byte[] { '\r', '\n' };

    public void doInitChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
        pipeline.addLast(new DelimiterBasedFrameDecoder(1024 * 1024, false, new ByteBuf[] {
                Unpooled.wrappedBuffer(new byte[] { 13 }), Unpooled.wrappedBuffer(new byte[] { 10 }) }));
        pipeline.addLast(new StringDecoder(charset));
        pipeline.addLast(new SimpleChannelInboundHandler<Object>() {
            @Override
            public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg == null)
                    return;
                String toJson = msg.toString();
                if (toJson.length() < 2)
                    return;
                JsonNode node = mapper.readTree(toJson);
                int command = Integer.parseInt(node.get("command").asText());
                String messageId = node.get("messageId").asText();
                String content = node.get("content").asText();
                long timestamp = node.get("timestamp").asLong();
                long now = System.currentTimeMillis();
                String sign = node.get("sign").asText();
                String data = "&" + command + "&" + messageId + "&" + content + "&" + timestamp + "&";
                boolean pass = (now - timestamp) < 5 * 60000
                        && RSAUtil.verify(data.getBytes(charset), serverPublicKey, sign);
                if (!pass) {
                    return;
                }
                if (command == TcpCommandEnums.LOGIN.getValue())
                    isLogin = true;
                else
                    tcpReceiver.read(command, messageId, content);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                ctx.close();
            }

            @Override
            public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                isLogin = false;
                if (exit) {
                    super.channelUnregistered(ctx);
                    return;
                }
                ctx.channel().closeFuture().sync();
                NettyTcpClient.this.reConnect();
            }

            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                if (evt instanceof IdleStateEvent) {
                    IdleStateEvent event = (IdleStateEvent) evt;
                    if (event.state() == IdleState.WRITER_IDLE) {
                        if (!isLogin)
                            return;
                        ByteBuf buf = Unpooled.buffer(HEARTBEAT.length);
                        buf.writeBytes(HEARTBEAT);
                        ctx.writeAndFlush(buf);
                    }
                } else {
                    super.userEventTriggered(ctx, evt);
                }
            }
        });
    }

    public void whenConnect() {
        //Tcp连接成功后进行登录：
        Map<Object, Object> content = new HashMap<>();
        String password = serverInfo.getPassword();
        content.put("password", password);
        this.writeCommand0(TcpCommandEnums.LOGIN.getValue(), "", content);
    }

    //通过该方法往云端写入数据
    public int writeCommand(int command, String messageId, Object content) {
        if (isLogin)
            return this.writeCommand0(command, messageId, content);
        return 1;
    }

    private int writeCommand0(int command, String messageId, Object content) {
        //找到对应的channel，并写入数据
        String unitId = this.serverInfo.getUnitId();
        long timestamp = System.currentTimeMillis();
        String contentStr = null;
        try {
            contentStr = mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            LOGGER.error("writeCommand0", e);
        }
        String data = "&" + unitId + "&" + command + "&" + messageId + "&" + contentStr + "&" + timestamp + "&";
        String privateKey = EnvironmentProps.getApplicationProp("tcp.client.privateKey");
        String sign = null;
        try {
            sign = RSAUtil.sign(data.getBytes(charset), privateKey);
        } catch (Exception e) {
            LOGGER.error("writeCommand0", e);
        }
        Map<Object, Object> toJson = new HashMap<>();
        toJson.put("unitId", unitId);
        toJson.put("command", command);
        toJson.put("content", contentStr);
        toJson.put("messageId", messageId);
        toJson.put("timestamp", timestamp);
        toJson.put("sign", sign);
        try {
            String json = mapper.writeValueAsString(toJson) + "\r\n";
            byte[] bytes = json.getBytes(charset);
            ByteBuf buf = Unpooled.buffer(bytes.length);
            buf.writeBytes(bytes);
            channel.writeAndFlush(buf);
            return 0;
        } catch (Exception e) {
            LOGGER.error("writeCommand0", e);
            return 1;
        }
    }

    //系统启动
    private void init() {
        this.eventLoopGroup = new NioEventLoopGroup(1);
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(eventLoopGroup).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        NettyTcpClient.this.doInitChannel(socketChannel);
                    }
                });
    }

    //系统退出
    public void unLoadDriver() {
        exit = true;
        if (this.eventLoopGroup != null) {
            //循环map，关闭所有channel
            this.eventLoopGroup.shutdownGracefully();
            this.bootstrap = null;
            this.eventLoopGroup = null;
        }
        executorConnect.shutdown();
    }

    /**
     * Connect.启动客户端方法
     *
     * @return the int
     */
    public int connect() {
        if (this.eventLoopGroup == null)
            this.init();
        LOGGER.info("进行netty-client连接......................");
        bootstrap.remoteAddress(this.serverInfo.getHost(), this.serverInfo.getPort());
        this.bootstrap.connect().addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                this.channel = future.channel();
                this.whenConnect();
                LOGGER.info("TCP.connect.success: IP: {}", this.serverInfo.getHost());
            } else {
                //this.eventLoopGroup.schedule(() -> this.connect(), 10, TimeUnit.SECONDS);
                LOGGER.error("TCP 设备IP:{} 连接失败，10秒后重新连接!", this.serverInfo.getHost());
            }
        });

        return 0;
    }

    private final ExecutorService executorConnect = Executors.newSingleThreadExecutor();

    private void reConnect() {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
        }
        executorConnect.execute(() -> this.connect());
    }
}
