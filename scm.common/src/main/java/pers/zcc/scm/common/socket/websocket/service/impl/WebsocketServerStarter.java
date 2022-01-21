package pers.zcc.scm.common.socket.websocket.service.impl;

import javax.inject.Named;

import pers.zcc.scm.common.frame.IAfterStartUpHandle;
import pers.zcc.scm.common.socket.websocket.ScmWebSocketServer;

@Named
public class WebsocketServerStarter implements IAfterStartUpHandle {

    @Override
    public void process() {
        ScmWebSocketServer instance = new ScmWebSocketServer(9005);
        instance.start();
        WebsocketServerHolder.add(instance);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
