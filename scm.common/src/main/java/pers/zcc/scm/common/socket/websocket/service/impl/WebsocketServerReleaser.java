package pers.zcc.scm.common.socket.websocket.service.impl;

import java.util.ArrayList;

import javax.inject.Named;

import pers.zcc.scm.common.frame.IBeforeShutDownHandle;
import pers.zcc.scm.common.socket.websocket.ScmWebSocketServer;

@Named
public class WebsocketServerReleaser implements IBeforeShutDownHandle {

    @Override
    public void process() {
        ArrayList<ScmWebSocketServer> instances = WebsocketServerHolder.get();
        if (!instances.isEmpty()) {
            for (ScmWebSocketServer instance : instances) {
                try {
                    instance.stop(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
