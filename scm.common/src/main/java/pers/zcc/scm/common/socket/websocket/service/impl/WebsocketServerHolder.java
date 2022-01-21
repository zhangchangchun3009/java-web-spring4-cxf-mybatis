package pers.zcc.scm.common.socket.websocket.service.impl;

import java.util.ArrayList;

import pers.zcc.scm.common.socket.websocket.ScmWebSocketServer;

public class WebsocketServerHolder {
    private static ArrayList<ScmWebSocketServer> serverInstances = new ArrayList<>();

    public static void add(ScmWebSocketServer instance) {
        serverInstances.add(instance);
    }

    public static ArrayList<ScmWebSocketServer> get() {
        return serverInstances;
    }
}
