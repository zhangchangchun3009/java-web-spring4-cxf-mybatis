package pers.zcc.scm.web.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import pers.zcc.scm.common.util.ApplicationContextManager;

/**
 * The Class UdpNioSocketServer.
 *
 * @author zhangchangchun
 * @Date 2022年6月20日
 */
public class UdpNioSocketServer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpNioSocketServer.class);

    public int port;

    private ThreadPoolTaskExecutor executor;

    private Selector selector;

    private DatagramChannel channel;

    private volatile boolean closeFlag;

    private ByteBuffer buf = ByteBuffer.allocate(65536);

    public UdpNioSocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        executor = (ThreadPoolTaskExecutor) ApplicationContextManager.getBean("threadPoolExecutor");
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            channel.register(selector, SelectionKey.OP_READ);
            LOGGER.info("NioSocketServer start at port {} success", port);
            while (!Thread.currentThread().isInterrupted() && !closeFlag) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey selectedKey = it.next();
                    try {
                        if (selectedKey.isReadable()) {
                            DatagramChannel dc = (DatagramChannel) selectedKey.channel();
                            buf.clear();
                            SocketAddress remoteAddr = dc.receive(buf);
                            buf.flip();
                            byte[] ba = new byte[buf.remaining()];
                            buf.get(ba);
                            executor.execute(new ParseContentTask(ba, dc, remoteAddr));
                        }
                    } catch (Exception e) {
                        LOGGER.error("UdpNioSocketServer read data failed,", e);
                    } finally {
                        it.remove();
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("UdpNioSocketServer run failed,", e);
        }
    }

    public void close() throws IOException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (selector != null && selector.isOpen()) {
            selector.close();
        }
        closeFlag = true;
    }

}
