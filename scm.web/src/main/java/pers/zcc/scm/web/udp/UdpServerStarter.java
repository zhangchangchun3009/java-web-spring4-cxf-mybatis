package pers.zcc.scm.web.udp;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import pers.zcc.scm.common.frame.IAfterStartUpHandle;

@Named
public class UdpServerStarter implements IAfterStartUpHandle {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(UdpServerStarter.class);

    @Value("${udp.server.port}")
    private int port;

    @Override
    public void process() {
        new Thread(new UdpNioSocketServer(port), "t-UdpServerStarter").start();
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
