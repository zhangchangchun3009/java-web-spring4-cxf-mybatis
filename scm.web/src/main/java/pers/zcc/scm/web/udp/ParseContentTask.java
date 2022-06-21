package pers.zcc.scm.web.udp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseContentTask implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(ParseContentTask.class);

    private byte[] orgMessageBytes;

    private SocketAddress remoteAddr;

    private DatagramChannel channel;

    public ParseContentTask(byte[] orgMessageBytes, DatagramChannel channel, SocketAddress remoteAddr) {
        this.orgMessageBytes = orgMessageBytes;
        this.channel = channel;
        this.remoteAddr = remoteAddr;
    }

    @Override
    public void run() {
        parseAndHandleMessage(orgMessageBytes);
    }

    private void parseAndHandleMessage(byte[] orgMessageBytes) {
        responsePacket(orgMessageBytes, (byte) 0x66);
    }

    private void responsePacket(byte[] orgMessageBytes, byte cmd) {
        byte[] responseByte = new byte[19];
        try {
            channel.send(ByteBuffer.wrap(responseByte), remoteAddr);
        } catch (IOException e) {
            LOGGER.error("udp send err", e);
        }
    }

}
