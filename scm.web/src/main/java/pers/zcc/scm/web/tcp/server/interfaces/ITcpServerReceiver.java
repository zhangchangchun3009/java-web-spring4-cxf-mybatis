package pers.zcc.scm.web.tcp.server.interfaces;

public interface ITcpServerReceiver {
    boolean login(String parkId, String json);

    void read(String parkId, int command, String messageId, String json);
}
