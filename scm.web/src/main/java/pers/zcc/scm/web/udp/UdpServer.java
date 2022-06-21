package pers.zcc.scm.web.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import pers.zcc.scm.common.util.ApplicationContextManager;

public class UdpServer implements Runnable {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UdpServer.class);

    public int port;

    private DatagramSocket ds = null;

    private ThreadPoolTaskExecutor executor;

    public UdpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        executor = (ThreadPoolTaskExecutor) ApplicationContextManager.getBean("threadPoolExecutor");
        InetSocketAddress socketAddress = new InetSocketAddress(port);
        try {
            ds = new DatagramSocket(socketAddress);
        } catch (SocketException e) {
            LOGGER.error("UdpServer SocketException", e);
        }
        LOGGER.info("udp server is starting at port {} ", port);
        while (true) {
            try {
                byte[] buffer = new byte[64];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet);
                byte orgMessageBytes[] = packet.getData();
                InetAddress receiveAddress = packet.getAddress();
                int receivePort = packet.getPort();
                executor.execute(new ParseAndResponseMessageTask(orgMessageBytes, receiveAddress, receivePort, ds));
            } catch (Exception e) {
                LOGGER.error("receive message error,", e);
            }
        }
    }

}
