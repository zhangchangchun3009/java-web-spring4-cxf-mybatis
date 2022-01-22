package pers.zcc.scm.web.tcp.client.interfaces;

public interface ITcpClientReceiver {
    void read(int command, String messageId, String content);
}
