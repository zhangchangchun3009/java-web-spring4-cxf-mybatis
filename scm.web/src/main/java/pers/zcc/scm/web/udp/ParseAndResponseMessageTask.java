package pers.zcc.scm.web.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseAndResponseMessageTask implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(ParseAndResponseMessageTask.class);

    private byte[] orgMessageBytes;

    private InetAddress receiveAddress;

    private int receivePort;

    private DatagramSocket ds;

    public ParseAndResponseMessageTask(byte[] orgMessageBytes, InetAddress receiveAddress, int receivePort,
            DatagramSocket ds) {
        this.orgMessageBytes = orgMessageBytes;
        this.receiveAddress = receiveAddress;
        this.receivePort = receivePort;
        this.ds = ds;
    }

    @Override
    public void run() {
        try {
            parseAndHandleMessage(orgMessageBytes, receiveAddress, receivePort);
        } catch (Exception e) {
            LOGGER.error("parse message error,", e);
        }
    }

    private void parseAndHandleMessage(byte[] orgMessageBytes, InetAddress receiveAddress, int receivePort) {
        responsePacket(orgMessageBytes, receiveAddress, receivePort, (byte) 0x66);
    }

    private void responsePacket(byte[] orgMessageBytes, InetAddress receiveAddress, int receivePort, byte cmd) {
        byte[] responseByte = new byte[19];
        DatagramPacket responsePacket = new DatagramPacket(responseByte, 0, responseByte.length, receiveAddress,
                receivePort);
        try {
            this.ds.send(responsePacket);
        } catch (IOException e) {
            LOGGER.error("udp send err", e);
        }
    }
}
