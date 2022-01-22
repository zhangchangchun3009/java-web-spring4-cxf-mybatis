package pers.zcc.scm.web.tcp.server;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import pers.zcc.scm.web.tcp.server.impl.BusinessHandler;

@Named
public class NettyTcpServer {
    @Inject
    private BusinessHandler businessHandler;

    @Value("${tcp.server.port:8030}")
    private int port;

    ServerBootstrap bootstrap = null;

    EventLoopGroup bossGroup = null;

    EventLoopGroup workerGroup = null;

    public void start() throws Exception {
        if (port == -1)
            return;
        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);

            bootstrap.option(ChannelOption.SO_BACKLOG, 2048); // 服务端排队队列
            bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024); // 设置发送缓冲大小
            bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 1024); // 这是接收缓冲大小
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // 保持连接:清除死连接，维持活跃的

            bootstrap.childOption(ChannelOption.TCP_NODELAY, true); // TCP无掩饰
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    //心跳检测(时间为10秒)
                    Charset charset = Charset.forName("UTF-8");
                    pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                    pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, false, new ByteBuf[] {
                            Unpooled.wrappedBuffer(new byte[] { 13 }), Unpooled.wrappedBuffer(new byte[] { 10 }) }));
                    pipeline.addLast(new StringDecoder(charset));
                    pipeline.addLast(businessHandler);
                }
            });
            bootstrap.bind(port).sync();
            System.out.println("TcpServer启动:" + port);
        } catch (Exception e) {
            System.out.println("Tcp服务启动失败！");
        }
    }

    public void stop() throws Exception {
        // 优雅退出 释放线程池资源
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        System.out.println("服务器优雅的释放了线程资源...");
    }
}
