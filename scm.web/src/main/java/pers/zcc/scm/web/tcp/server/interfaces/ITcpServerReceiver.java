package pers.zcc.scm.web.tcp.server.interfaces;

public interface ITcpServerReceiver {
    boolean login(String unitId, String json);

    void read(String unitId, int command, String messageId, String json);
}
