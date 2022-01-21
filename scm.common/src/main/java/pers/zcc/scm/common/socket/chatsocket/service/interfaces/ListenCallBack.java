package pers.zcc.scm.common.socket.chatsocket.service.interfaces;

import java.net.Socket;

public interface ListenCallBack {

    void onAccept(Socket socket, String message);

}
